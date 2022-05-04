package no.divvun.divvunspell;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

class CLibrary {
    public static native void divvun_enable_logging();

    public static native TraitObjectPointer.ByValue divvun_speller_archive_open(PathPointer.ByValue path, ErrorCallback error);

    public static native TraitObjectPointer.ByValue divvun_speller_archive_speller(TraitObjectPointer.ByValue handle, ErrorCallback error);

    public static native boolean divvun_speller_is_correct(TraitObjectPointer.ByValue speller, StringPointer.ByValue word, ErrorCallback error);

    public static native SlicePointer.ByValue divvun_speller_suggest(TraitObjectPointer.ByValue speller, StringPointer.ByValue word, ErrorCallback error);

    public static native NativeLong divvun_vec_suggestion_len(SlicePointer.ByValue suggestions, ErrorCallback error);

    public static native StringPointer.ByValue divvun_vec_suggestion_get_value(SlicePointer.ByValue suggestions, NativeLong index, ErrorCallback error);

    public static native void cffi_string_free(StringPointer.ByValue ptr);

    static {
        Native.register(CLibrary.class, "divvunspell");
    }
}