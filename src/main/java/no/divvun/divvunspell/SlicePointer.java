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
public class SlicePointer extends Structure {
    public volatile Pointer data;
    public volatile Pointer len;

    public static class ByValue extends SlicePointer implements Structure.ByValue {

    }
}
