package com.jd.journalq.store.transaction;

import com.jd.journalq.store.PartialLogException;
import com.jd.journalq.store.ReadException;
import com.jd.journalq.store.file.LogSerializer;

import java.nio.ByteBuffer;

/**
 * @author liyue25
 * Date: 2018-11-28
 */
public class TransactionMessageSerializer implements LogSerializer<ByteBuffer> {


    private ByteBuffer read(ByteBuffer src) {

        if (src.remaining() >= Integer.BYTES) {
            int length = src.getInt(src.position());
            return readByLength(src, length);
        }
        throw new ReadException();

    }

    @Override
    public ByteBuffer read(ByteBuffer src, int length) {
        src.mark();
        try {
            if (length < 0) {
                return read(src);
            } else {
                return readByLength(src, length);
            }
        } catch (Throwable t) {
            src.reset();
            throw t;
        }
    }

    private ByteBuffer readByLength(ByteBuffer src, int length) {
        ByteBuffer buffer;
        if (length > Integer.BYTES) {
            if (src.remaining() < length) throw new PartialLogException();
            byte[] readBuffer = new byte[length];
            src.get(readBuffer, 0, length);
            buffer = ByteBuffer.wrap(readBuffer);
            return buffer;
        }
        throw new ReadException();
    }

    /**
     * 从src中读取若干条Log，并返回这些Log的总长度
     *
     * @param src    存放消息的ByteBuffer，调用此方法不改变src的position、mark和limit
     * @param length 最多读取Log的总长度
     * @return 返回若干条消息，消息的条数不固定，但满足如下全部条件：
     * 1. Log总长度不超过length
     * 2. Log总长度不超过src剩余的字节数
     */
    @Override
    public int trim(ByteBuffer src, int length) {
        ByteBuffer sliced = src.slice();
        int pos = 0;
        int lengthOfSrc = sliced.remaining();
        int vRemaining;
        while ((vRemaining = lengthOfSrc - pos) > Integer.BYTES
                && pos < length) {
            int len = sliced.getInt(pos);
            if (len > Integer.BYTES) {
                if (vRemaining < len) {
                    break;
                }
                pos += len;
            } else {
                throw new ReadException();
            }
        }
        return pos;
    }

    @Override
    public int size(ByteBuffer buffer) {
        return buffer.remaining();
    }

    @Override
    public int append(ByteBuffer from, ByteBuffer to) {
        int length = from.remaining();
        from.mark();
        to.put(from);
        from.reset();
        return length;
    }

}
