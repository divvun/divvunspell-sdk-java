package no.divvun.divvunspell;

import com.sun.jna.Memory;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_16;
import static java.nio.charset.StandardCharsets.UTF_8;

@Structure.FieldOrder({ "data", "len" })
public class SlicePointer extends Structure {
    public volatile Pointer data;
    public volatile Pointer len;

    public static class ByValue extends SlicePointer implements Structure.ByValue {
        @NotNull public static SlicePointer.ByValue encode(String value) {
            SlicePointer.ByValue ptr = new SlicePointer.ByValue();
            byte[] bytes = value.getBytes(UTF_8);
            int length = bytes.length;
            Memory data = new Memory(length);
            data.write(0, bytes, 0, length);

            ptr.data = data;
            ptr.len = Pointer.createConstant(length);

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

        byte[] array = data.getByteArray(0, (int)v);
        return new String(array, UTF_8);
    }
}
