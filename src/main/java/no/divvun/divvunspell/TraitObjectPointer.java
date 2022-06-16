package no.divvun.divvunspell;

import com.sun.jna.*;

@Structure.FieldOrder({ "data", "vtable" })
public class TraitObjectPointer extends Structure {
    public volatile NativeLong data;
    public volatile NativeLong vtable;

    public static class ByValue extends TraitObjectPointer implements Structure.ByValue {

    }
}
