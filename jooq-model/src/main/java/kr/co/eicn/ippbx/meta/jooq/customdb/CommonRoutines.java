package kr.co.eicn.ippbx.meta.jooq.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.routines.FnDecStringText;
import kr.co.eicn.ippbx.meta.jooq.customdb.routines.FnEncStringText;
import org.jooq.Configuration;
import org.jooq.Field;

public class CommonRoutines {

    /**
     * Call <code>CUSTOMDB.fn_dec_string_text</code>
     */
    public static String fnDecStringText(Configuration configuration, String stringtext, String secretkey) {
        FnDecStringText f = new FnDecStringText();
        f.setStringtext(stringtext);
        f.setSecretkey(secretkey);

        f.execute(configuration);
        return f.getReturnValue();
    }

    /**
     * Get <code>CUSTOMDB.fn_dec_string_text</code> as a field.
     */
    public static Field<String> fnDecStringText(String stringtext, String secretkey) {
        FnDecStringText f = new FnDecStringText();
        f.setStringtext(stringtext);
        f.setSecretkey(secretkey);

        return f.asField();
    }

    /**
     * Get <code>CUSTOMDB.fn_dec_string_text</code> as a field.
     */
    public static Field<String> fnDecStringText(Field<String> stringtext, Field<String> secretkey) {
        FnDecStringText f = new FnDecStringText();
        f.setStringtext(stringtext);
        f.setSecretkey(secretkey);

        return f.asField();
    }

    /**
     * Get <code>CUSTOMDB.fn_dec_string_text</code> as a field.
     */
    public static Field<String> fnDecStringText(Field<String> stringtext, String secretkey) {
        FnDecStringText f = new FnDecStringText();
        f.setStringtext(stringtext);
        f.setSecretkey(secretkey);

        return f.asField();
    }

    /**
     * Call <code>CUSTOMDB.fn_enc_string_text</code>
     */
    public static String fnEncStringText(Configuration configuration, String stringtext, String secretkey) {
        FnEncStringText f = new FnEncStringText();
        f.setStringtext(stringtext);
        f.setSecretkey(secretkey);

        f.execute(configuration);
        return f.getReturnValue();
    }

    /**
     * Get <code>CUSTOMDB.fn_enc_string_text</code> as a field.
     */
    public static Field<String> fnEncStringText(String stringtext, String secretkey) {
        FnEncStringText f = new FnEncStringText();
        f.setStringtext(stringtext);
        f.setSecretkey(secretkey);

        return f.asField();
    }

    /**
     * Get <code>CUSTOMDB.fn_enc_string_text</code> as a field.
     */
    public static Field<String> fnEncStringText(Field<String> stringtext, Field<String> secretkey) {
        FnEncStringText f = new FnEncStringText();
        f.setStringtext(stringtext);
        f.setSecretkey(secretkey);

        return f.asField();
    }
}
