package pro.prieran.misis.gs.hamming

import java.util.*
import kotlin.experimental.and
import kotlin.math.min
import kotlin.math.pow

class BitSequence {

    private val sequence: MutableList<Boolean>

    constructor() {
        sequence = mutableListOf()
    }

    constructor(message: String) {
        sequence = message.toByteArray().flatMap { it.bits() }.toMutableList()
    }

    constructor(bitSequence: List<Boolean>) {
        sequence = bitSequence.toMutableList()
    }

    constructor(other: BitSequence) {
        sequence = other.sequence.toMutableList()
    }

    constructor(bitSequence: List<Boolean>, targetSize: Int) : this(bitSequence) {
        while (sequence.size < targetSize) {
            sequence.add(false)
        }
    }

    fun size() = sequence.size

    fun insert(index: Int, value: Boolean) = sequence.add(index, value)

    fun remove(index: Int) = sequence.removeAt(index)

    fun append(sequence: BitSequence) = this.sequence.addAll(sequence.sequence)

    fun toggle(index: Int) {
        sequence[index] = !sequence[index]
    }

    fun subSequence(startInclusive: Int, endNotInclusive: Int, length: Int): BitSequence {
        if (startInclusive > endNotInclusive) {
            throw IndexOutOfBoundsException("Start index is more than end")
        }
        if (startInclusive < 0 || endNotInclusive > size()) {
            throw IndexOutOfBoundsException("Indexes of subsequence are out of range")
        }
        return BitSequence(sequence.subList(startInclusive, endNotInclusive), length)
    }

    fun get(index: Int): Boolean {
        if (index < 0 || index >= sequence.size) {
            throw IndexOutOfBoundsException("Index of getter is out of range")
        }
        return sequence.get(index)
    }

    fun set(index: Int, value: Boolean) {
        if (index < 0 || index > sequence.size - 1) {
            throw IndexOutOfBoundsException("Index of getter is out of range")
        }
        sequence.set(index, value)
    }

    fun addRandomNoise(count: Int) {
        val random = Random()

        for (i in 0.until(count)) {
            val index = random.nextInt(sequence.size)
            toggle(index)
        }
    }

    override fun toString() = sequence.map {
        if (it) {
            '1'
        } else {
            '0'
        }
    }.joinToString(separator = "")


    private fun Byte.bits(): List<Boolean> {
        val array = mutableListOf<Boolean>()

        for (i in (0..7).reversed()) {
            array.add(this.and(2.0.pow(i).toByte()) == 2.0.pow(i).toByte())
        }
        return array
    }

    fun bytes(): ByteArray {
        if (sequence.size % 8 != 0) {
            throw InstantiationError("Sequence length doesn't devides on eight")
        }

        val array = mutableListOf<Byte>()

        for (i in 0.until(sequence.size / 8)) {
            array.add(subSequence(i * 8, i * 8 + 8, 8).toString().toInt(2).toByte())
        }
        return array.toByteArray()
    }
}

class HammingEncoder(private val length: Int) {

    var len = 0

    fun encode(message: String) = splitSourceToParts(BitSequence(message))
            .also { it.forEach { insertVerificationBits(it) } }
            .let { uniteMessages(it) }

    fun encodeWithNoise(message: String, errorsPerWord: Int) = splitSourceToParts(BitSequence(message))
            .also {
                it.forEach {
                    insertVerificationBits(it)
                    it.addRandomNoise(errorsPerWord)
                }
            }
            .let { uniteMessages(it) }

    fun decode(message: BitSequence) = splitResultToParts(message)
            .also { it.forEach { removeVerificationBits(it) } }
            .let { uniteMessages(it) }
            .let { cropMessage(it) }
            .bytes()
            .let { String(it) }

    fun restore(message: BitSequence) = splitResultToParts(message)
            .also { it.forEach { removeVerificationBitsAndRestore(it) } }
            .let { uniteMessages(it) }
            .let { cropMessage(it) }
            .bytes()
            .let { String(it) }


    private fun cropMessage(sequence: BitSequence) = sequence.subSequence(0, len, len)

    private fun splitSourceToParts(sequence: BitSequence): List<BitSequence> {
        val messageParts = mutableListOf<BitSequence>()

        for (i in 0..(sequence.size() / length)) {
            if (i * length + length < sequence.size()) {
                messageParts.add(sequence.subSequence(i * length, i * length + length, length))
            } else {
                len = sequence.size()
                messageParts.add(sequence.subSequence(i * length, sequence.size(), length))
            }
        }

        return messageParts
    }

    private fun splitResultToParts(sequence: BitSequence): List<BitSequence> {
        val messageParts = mutableListOf<BitSequence>()

        val partLength = length + length.powersOf2().size
        val countOfParts = sequence.size() / partLength

        for (i in 0.until(countOfParts)) {
            messageParts.add(sequence.subSequence(i * partLength, (i + 1) * partLength, partLength))
        }

        return messageParts
    }

    private fun uniteMessages(sequences: List<BitSequence>) = BitSequence().apply { sequences.forEach { this.append(it) } }

    private fun calcCheckSum(sequence: BitSequence, indexes: List<Int>): List<Boolean> {
        val checkSums = mutableListOf<Boolean>()

        for (i in indexes) {
            var sum = false
            var position = i - 1

            while (position < sequence.size()) {
                var split = 0
                while (position + split < min(sequence.size(), position + i)) {
                    sum = sum.xor(sequence.get(position + split))
                    split++
                }
                position += 2 * i
            }
            checkSums.add(sum)
        }

        return checkSums
    }

    private fun insertVerificationBits(sequence: BitSequence) {
        val indexes = sequence.size().powersOf2()

        for (i in 0.until(indexes.size)) {
            sequence.insert(indexes[i] - 1, false)
        }

        val checkSums = calcCheckSum(sequence, indexes)

        for (i in 0.until(indexes.size)) {
            sequence.set(indexes[i] - 1, checkSums[i])
        }
    }

    private fun removeVerificationBits(sequence: BitSequence) {
        val indexes = length.powersOf2().reversed()

        for (i in 0.until(indexes.size)) {
            sequence.remove(indexes[i] - 1)
        }
    }

    private fun removeVerificationBitsAndRestore(sequence: BitSequence) {
        val indexes = length.powersOf2()

        val sequenceCopy = BitSequence(sequence)

        for (i in 0.until(indexes.size)) {
            sequenceCopy.set(indexes[i] - 1, false)
        }

        val calculatedChecksum = calcCheckSum(sequenceCopy, indexes)

        val writtenChecksum = mutableListOf<Boolean>()

        for (i in 0.until(indexes.size)) {
            writtenChecksum.add(sequence.get(indexes[i] - 1))
        }

        var index = 0
        for (i in 0.until(calculatedChecksum.size)) {
            if (calculatedChecksum[i] != writtenChecksum[i]) {
                index += 2.toFloat().pow(i).toInt()
            }
        }


        if (index > 0) {
            if (index > sequence.size()) {
                throw Exception("More than one error is detected")
            } else {
                sequence.toggle(index - 1)
            }
        } else {
            println("No errors")
        }

        removeVerificationBits(sequence)
    }

    private fun Int.powersOf2(): List<Int> {
        val powers = mutableListOf<Int>()
        for (i in 0.until(this)) {
            val nextPower = 2.0.pow(i).toInt()
            if (nextPower - this >= 0) {
                break
            } else {
                powers.add(nextPower)
            }
        }
        return powers
    }
}
