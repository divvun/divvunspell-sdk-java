package no.divvun.divvunspell;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

class CLibrary {
    public static native Pointer divvun_hfst_zip_speller_archive_open(PathPointer.ByValue path, ErrorCallback error);

    public static native Pointer divvun_hfst_zip_speller_archive_speller(Pointer handle, ErrorCallback error);

    public static native boolean divvun_hfst_zip_speller_is_correct(Pointer speller, StringPointer.ByValue word, ErrorCallback error);

    public static native SlicePointer.ByValue divvun_hfst_zip_speller_suggest(Pointer speller, StringPointer.ByValue word, ErrorCallback error);

    public static native Pointer divvun_thfst_chunked_box_speller_archive_open(PathPointer.ByValue path, ErrorCallback error);

    public static native Pointer divvun_thfst_chunked_box_speller_archive_speller(Pointer handle, ErrorCallback error);

    public static native boolean divvun_thfst_chunked_box_speller_is_correct(Pointer speller, StringPointer.ByValue word, ErrorCallback error);

    public static native SlicePointer.ByValue divvun_thfst_chunked_box_speller_suggest(Pointer speller, StringPointer.ByValue word, ErrorCallback error);

    //    fun divvun_thfst_chunked_box_speller_suggest_with_config(speller: Pointer, word: SlicePointer.ByValue, config: CSpellerConfig, error: ErrorCallback): SlicePointer
    public static native NativeLong divvun_vec_suggestion_len(SlicePointer.ByValue suggestions, ErrorCallback error);

    public static native StringPointer.ByValue divvun_vec_suggestion_get_value(SlicePointer.ByValue suggestions, NativeLong index, ErrorCallback error);

    public static native void cffi_string_free(StringPointer.ByValue ptr);

    static {
        Native.register(CLibrary.class, "divvunspell");
    }
}