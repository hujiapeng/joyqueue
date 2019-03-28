package com.jd.journalq.toolkit.buffer;


import com.jd.journalq.toolkit.buffer.bytes.MappedBytes;
import com.jd.journalq.toolkit.buffer.memory.MappedMemory;
import com.jd.journalq.toolkit.buffer.memory.MappedMemoryAllocator;
import com.jd.journalq.toolkit.buffer.memory.Memory;
import com.jd.journalq.toolkit.lang.Preconditions;
import com.jd.journalq.toolkit.ref.ReferenceManager;

import java.io.File;
import java.nio.channels.FileChannel;

/**
 * Memory mapped file buffer.
 * <p>
 * Mapped buffers provide direct access to memory from allocated by a {@link java.nio.MappedByteBuffer}. Memory is
 * allocated
 * by opening and expanding the given {@link java.io.File} to the desired {@code count} and mapping the file contents
 * into memory
 */
public class MappedBuffer extends NativeBuffer {
    private static final long DEFAULT_INITIAL_CAPACITY = 1024 * 1024 * 16;
    public static final String INITIAL_CAPACITY_ERROR_MESSAGE =
            "initial capacity for MappedBuffer cannot be greater than " + MappedMemory.SIZE_MAX;
    public static final String MAX_CAPACITY_ERROR_MESSAGE =
            "maximum capacity for MappedBuffer cannot be greater than " + MappedMemory.SIZE_MAX;

    /**
     * Allocates a dynamic capacity mapped buffer in {@link java.nio.channels.FileChannel.MapMode#READ_WRITE} mode with
     * an initial capacity
     * of {@code 16MiB} and a maximum capacity of {@link Integer#MAX_VALUE}.
     * <p>
     * The resulting buffer will have a maximum capacity of {@link Integer#MAX_VALUE}. As bytes are written to the
     * buffer
     * its capacity will double in count each time the current capacity is reached. Memory will be mapped by opening and
     * expanding the given {@link java.io.File} to the desired {@code capacity} and mapping the file contents into
     * memory via
     * {@link java.nio.channels.FileChannel#map(java.nio.channels.FileChannel.MapMode, long, long)}.
     *
     * @param file The file to map into memory.
     * @return The mapped buffer.
     * @throws NullPointerException If {@code file} is {@code null}
     * @see MappedBuffer#allocate(java.io.File, java.nio.channels.FileChannel.MapMode)
     * @see MappedBuffer#allocate(java.io.File, long)
     * @see MappedBuffer#allocate(java.io.File, java.nio.channels.FileChannel.MapMode, long)
     * @see MappedBuffer#allocate(java.io.File, long, long)
     * @see MappedBuffer#allocate(java.io.File, java.nio.channels.FileChannel.MapMode, long, long)
     */
    public static MappedBuffer allocate(final File file) {
        return allocate(file, MappedMemoryAllocator.DEFAULT_MAP_MODE, DEFAULT_INITIAL_CAPACITY, Long.MAX_VALUE);
    }

    /**
     * Allocates a dynamic capacity mapped buffer in {@link java.nio.channels.FileChannel.MapMode#READ_WRITE} mode with
     * an initial capacity
     * of {@code 16MiB} and a maximum capacity of {@link Integer#MAX_VALUE}.
     * <p>
     * The resulting buffer will be initialized to a capacity of {@code 4096} and have a maximum capacity of
     * {@link Integer#MAX_VALUE}. As bytes are written to the buffer its capacity will double in count each time the
     * current
     * capacity is reached. Memory will be mapped by opening and expanding the given {@link java.io.File} to the desired
     * {@code capacity} and mapping the file contents into memory via
     * {@link java.nio.channels.FileChannel#map(java.nio.channels.FileChannel.MapMode, long, long)}.
     *
     * @param file The file to map into memory.
     * @param mode The mode with which to map the file.
     * @return The mapped buffer.
     * @throws NullPointerException If {@code file} is {@code null}
     * @see MappedBuffer#allocate(java.io.File)
     * @see MappedBuffer#allocate(java.io.File, long)
     * @see MappedBuffer#allocate(java.io.File, java.nio.channels.FileChannel.MapMode, long)
     * @see MappedBuffer#allocate(java.io.File, long, long)
     * @see MappedBuffer#allocate(java.io.File, java.nio.channels.FileChannel.MapMode, long, long)
     */
    public static MappedBuffer allocate(final File file, final FileChannel.MapMode mode) {
        return allocate(file, mode, DEFAULT_INITIAL_CAPACITY, Long.MAX_VALUE);
    }

    /**
     * Allocates a fixed capacity mapped buffer in {@link java.nio.channels.FileChannel.MapMode#READ_WRITE} mode.
     * <p>
     * Memory will be mapped by opening and expanding the given {@link java.io.File} to the desired {@code capacity}
     * and mapping the
     * file contents into memory via
     * {@link java.nio.channels.FileChannel#map(java.nio.channels.FileChannel.MapMode, long, long)}.
     * <p>
     * The resulting buffer will have a capacity of {@code capacity}. The underlying {@link MappedBytes} will be
     * initialized to the next power of {@code 2}.
     *
     * @param file     The file to map into memory.
     * @param capacity The fixed capacity of the buffer to allocate (in bytes).
     * @return The mapped buffer.
     * @throws NullPointerException     If {@code file} is {@code null}
     * @throws IllegalArgumentException If the {@code capacity} is greater than {@link Integer#MAX_VALUE}.
     * @see MappedBuffer#allocate(java.io.File)
     * @see MappedBuffer#allocate(java.io.File, java.nio.channels.FileChannel.MapMode)
     * @see MappedBuffer#allocate(java.io.File, java.nio.channels.FileChannel.MapMode, long)
     * @see MappedBuffer#allocate(java.io.File, long, long)
     * @see MappedBuffer#allocate(java.io.File, java.nio.channels.FileChannel.MapMode, long, long)
     */
    public static MappedBuffer allocate(final File file, final long capacity) {
        return allocate(file, MappedMemoryAllocator.DEFAULT_MAP_MODE, capacity, capacity);
    }

    /**
     * Allocates a fixed capacity mapped buffer in the given {@link java.nio.channels.FileChannel.MapMode}.
     * <p>
     * Memory will be mapped by opening and expanding the given {@link java.io.File} to the desired {@code capacity}
     * and mapping the
     * file contents into memory via
     * {@link java.nio.channels.FileChannel#map(java.nio.channels.FileChannel.MapMode, long, long)}.
     * <p>
     * The resulting buffer will have a capacity of {@code capacity}. The underlying {@link MappedBytes} will be
     * initialized to the next power of {@code 2}.
     *
     * @param file     The file to map into memory.
     * @param mode     The mode with which to map the file.
     * @param capacity The fixed capacity of the buffer to allocate (in bytes).
     * @return The mapped buffer.
     * @throws NullPointerException     If {@code file} is {@code null}
     * @throws IllegalArgumentException If the {@code capacity} is greater than {@link Integer#MAX_VALUE}.
     * @see MappedBuffer#allocate(java.io.File)
     * @see MappedBuffer#allocate(java.io.File, java.nio.channels.FileChannel.MapMode)
     * @see MappedBuffer#allocate(java.io.File, long)
     * @see MappedBuffer#allocate(java.io.File, long, long)
     * @see MappedBuffer#allocate(java.io.File, java.nio.channels.FileChannel.MapMode, long, long)
     */
    public static MappedBuffer allocate(final File file, final FileChannel.MapMode mode, final long capacity) {
        return allocate(file, mode, capacity, capacity);
    }

    /**
     * Allocates a mapped buffer.
     * <p>
     * Memory will be mapped by opening and expanding the given {@link java.io.File} to the desired {@code count} and
     * mapping the
     * file contents into memory via
     * {@link java.nio.channels.FileChannel#map(java.nio.channels.FileChannel.MapMode, long, long)}.
     * <p>
     * The resulting buffer will have a capacity of {@code initialCapacity}. The underlying {@link MappedBytes} will be
     * initialized to the next power of {@code 2}. As bytes are written to the buffer, the buffer's capacity will double
     * as long as {@code maxCapacity > capacity}.
     *
     * @param file            The file to map into memory. If the file doesn't exist it will be automatically created.
     * @param initialCapacity The initial capacity of the buffer.
     * @param maxCapacity     The maximum capacity of the buffer.
     * @return The mapped buffer.
     * @throws NullPointerException     If {@code file} is {@code null}
     * @throws IllegalArgumentException If the {@code capacity} or {@code maxCapacity} is greater than
     *                                  {@link Integer#MAX_VALUE}.
     * @see MappedBuffer#allocate(java.io.File)
     * @see MappedBuffer#allocate(java.io.File, java.nio.channels.FileChannel.MapMode)
     * @see MappedBuffer#allocate(java.io.File, long)
     * @see MappedBuffer#allocate(java.io.File, java.nio.channels.FileChannel.MapMode, long)
     * @see MappedBuffer#allocate(java.io.File, java.nio.channels.FileChannel.MapMode, long, long)
     */
    public static MappedBuffer allocate(final File file, final long initialCapacity, final long maxCapacity) {
        return allocate(file, MappedMemoryAllocator.DEFAULT_MAP_MODE, initialCapacity, maxCapacity);
    }

    /**
     * Allocates a mapped buffer.
     * <p>
     * Memory will be mapped by opening and expanding the given {@link java.io.File} to the desired {@code count} and
     * mapping the
     * file contents into memory via
     * {@link java.nio.channels.FileChannel#map(java.nio.channels.FileChannel.MapMode, long, long)}.
     * <p>
     * The resulting buffer will have a capacity of {@code initialCapacity}. The underlying {@link MappedBytes} will be
     * initialized to the next power of {@code 2}. As bytes are written to the buffer, the buffer's capacity will double
     * as long as {@code maxCapacity > capacity}.
     *
     * @param file            The file to map into memory. If the file doesn't exist it will be automatically created.
     * @param mode            The mode with which to map the file.
     * @param initialCapacity The initial capacity of the buffer.
     * @param maxCapacity     The maximum capacity of the buffer.
     * @return The mapped buffer.
     * @throws NullPointerException     If {@code file} is {@code null}
     * @throws IllegalArgumentException If the {@code capacity} or {@code maxCapacity} is greater than
     *                                  {@link Integer#MAX_VALUE}.
     * @see MappedBuffer#allocate(java.io.File)
     * @see MappedBuffer#allocate(java.io.File, java.nio.channels.FileChannel.MapMode)
     * @see MappedBuffer#allocate(java.io.File, long)
     * @see MappedBuffer#allocate(java.io.File, java.nio.channels.FileChannel.MapMode, long)
     * @see MappedBuffer#allocate(java.io.File, long, long)
     */
    public static MappedBuffer allocate(final File file, final FileChannel.MapMode mode, final long initialCapacity,
            final long maxCapacity) {
        Preconditions.checkNotNull(file, "file cannot be null");
        Preconditions.checkArgument(initialCapacity <= maxCapacity,
                "initial capacity cannot be greater than maximum capacity");
        Preconditions.checkArgument(initialCapacity <= MappedMemory.SIZE_MAX, INITIAL_CAPACITY_ERROR_MESSAGE);
        Preconditions.checkArgument(maxCapacity <= MappedMemory.SIZE_MAX, MAX_CAPACITY_ERROR_MESSAGE);
        return new MappedBuffer(new MappedBytes(file, MappedMemory
                .allocate(file, mode == null ? MappedMemoryAllocator.DEFAULT_MAP_MODE : mode,
                        Memory.Util.toPow2(initialCapacity))), 0, initialCapacity, maxCapacity);
    }

    protected MappedBuffer(MappedBytes bytes, ReferenceManager<Buffer> referenceManager) {
        super(bytes, referenceManager);
    }

    MappedBuffer(MappedBytes bytes, long offset, long initialCapacity, long maxCapacity) {
        super(bytes, offset, initialCapacity, maxCapacity);
    }

    /**
     * Deletes the underlying file.
     */
    public void delete() {
        ((MappedBytes) bytes).delete();
    }

}