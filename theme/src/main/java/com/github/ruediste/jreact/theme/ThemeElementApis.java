package com.github.ruediste.jreact.theme;

import com.github.ruediste.jreact.CoreElementApis;
import com.github.ruediste.jreact.style.CoreTokenApis.ColorHelper;
import com.github.ruediste.jreact.style.CoreTokenApis.ColorTokenApi;
import com.github.ruediste.jreact.style.CoreTokenApis.ColorTokenApiHelper;
import com.github.ruediste.jreact.utility.ColorPaletteApis.ColorPaletteApi;
import com.github.ruediste.jreact.utility.UtilityElementApis;

public interface ThemeElementApis {

    public interface ElementThemeApiHelper<Api>
            extends UtilityElementApis.ElementUtilityApiHelper<Api>,
            ColorTokenApiHelper<CombinedColorTokens<Api, CombinedColorTokensHelper<Api>>> {
    }

    public interface CombinedColorTokensHelper<Api>
            extends ColorHelper<Api>, ColorTokens.ColorValuesHelper {
    }

    public interface CombinedColorTokens<Api, Helper extends CombinedColorTokensHelper<Api>>
            extends ColorTokens<Api, Helper>, ColorPaletteApi<Api, Helper> {
    }

    public interface ElementThemeApiBase<Self, Helper extends ElementThemeApiHelper<Self>>
            extends UtilityElementApis.ElementUtilityApiBase<Self, Helper>,
            ColorTokenApi<Self, CombinedColorTokens<Self, CombinedColorTokensHelper<Self>>, Helper> {

    }

    public interface ElementWithChildrenThemeApiBase<Self, //
            Helper extends ElementThemeApiHelper<Self> & CoreElementApis.ChildrenApiHelper<Self>>
            extends ElementThemeApiBase<Self, Helper>, CoreElementApis.ChildrenApi<Self, Helper> {

    }

}
