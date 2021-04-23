package no.divvun.divvunspell;

import com.sun.jna.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.*;

@Structure.FieldOrder({ "data", "len" })
public class StringPointer extends Structure {
    public volatile Pointer data;
    public volatile Pointer len;

    public static class ByValue extends StringPointer implements Structure.ByValue {
        @NotNull public static StringPointer.ByValue encode(String value) {
            StringPointer.ByValue ptr = new StringPointer.ByValue();
            byte[] bytes = value.getBytes(UTF_8);
            Memory data = new Memory(bytes.length);
            data.write(0, bytes, 0, bytes.length);

            ptr.data = data;
            ptr.len = Pointer.createConstant(bytes.length);

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
