package no.divvun.divvunspell;

import com.sun.jna.*;

@Structure.FieldOrder({ "data", "len" })
public class SlicePointer extends Structure {
    public volatile Pointer data;
    public volatile Pointer len;

    public static class ByValue extends SlicePointer implements Structure.ByValue {

    }
}
