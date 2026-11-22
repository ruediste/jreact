package com.github.ruediste.jreact.utility;

import com.github.ruediste.jreact.color.JColor;
import com.github.ruediste.jreact.color.OkLch;
import com.github.ruediste.jreact.style.CoreTokenElementApis.ColorHelper;
import com.github.ruediste.jreact.utility.ColorPaletteTokenApis.Combined;

public class ColorPalette {
    public static Combined<OkLch, ?> COLOR_PALETTE = new Values();

    private static class Values implements Combined<OkLch, Values>, ColorHelper<OkLch> {

        @Override
        public Values _helper() {
            return this;
        }

        @Override
        public OkLch color(JColor color) {
            return color.toOkLch();
        }
    }

}
