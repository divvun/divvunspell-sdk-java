package no.divvun.divvunspell;

import com.sun.jna.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.*;

@Structure.FieldOrder({ "data", "len" })
public class PathPointer extends Structure {
    public volatile Pointer data;
    public volatile Pointer len;

    public static class ByValue extends PathPointer implements Structure.ByValue {
        @NotNull public static PathPointer.ByValue encode(String value) {
            String osName = System.getProperty("os.name", "generic").toLowerCase();
            PathPointer.ByValue ptr = new PathPointer.ByValue();
            byte[] bytes = value.getBytes(getCharset());

            if (osName.contains("win"))
            {
                ByteBuffer buffer = ByteBuffer.wrap(bytes);
                buffer.order(ByteOrder.LITTLE_ENDIAN);

                short[] arr = new short[bytes.length / 2];
                buffer.asShortBuffer().get(arr);
                Memory data = new Memory(bytes.length);
                data.write(0, arr, 0, arr.length);

                ptr.data = data;
                ptr.len = Pointer.createConstant(arr.length);

            }
            else
            {
                Memory data = new Memory(bytes.length);
                data.write(0, bytes, 0, bytes.length);

                ptr.data = data;
                ptr.len = Pointer.createConstant(bytes.length);
            }

            ptr.writeField("data");
            ptr.writeField("len");

            return ptr;
        }
    }

    @Nullable
    public String decode() {
        long v = Pointer.nativeValue(len);

        if (v == 0 || data == Pointer.NULL) {
            return null;
        }

        String osName = System.getProperty("os.name", "generic").toLowerCase();
        if (osName.contains("win"))
        {
            short[] array = data.getShortArray(0, (int)v);
            ByteBuffer buffer = ByteBuffer.allocate((int)v * 2);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.asShortBuffer().put(array);

            return new String(buffer.array(), getCharset());

        } else {
            byte[] array = data.getByteArray(0, (int)v);
            return new String(array, getCharset());
        }

    }

    private static Charset getCharset() {
        String osName = System.getProperty("os.name", "generic").toLowerCase();

        if (osName.contains("win"))
        {
            return UTF_16LE;
        }
        else
        {
            return UTF_8;
        }
    }
}
