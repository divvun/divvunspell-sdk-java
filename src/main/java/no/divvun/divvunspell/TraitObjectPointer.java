package no.divvun.divvunspell;

import com.sun.jna.*;

@Structure.FieldOrder({ "data", "vtable" })
public class TraitObjectPointer extends Structure {
    public volatile Pointer data;
    public volatile Pointer vtable;

    public static class ByValue extends TraitObjectPointer implements Structure.ByValue {

    }
}
