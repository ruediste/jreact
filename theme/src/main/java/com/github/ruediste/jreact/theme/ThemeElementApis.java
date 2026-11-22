package com.github.ruediste.jreact.theme;

import com.github.ruediste.jreact.CoreElementApis;
import com.github.ruediste.jreact.style.CoreTokenElementApis.ColorHelper;
import com.github.ruediste.jreact.style.CoreTokenElementApis.ColorTokenElementApi;
import com.github.ruediste.jreact.style.CoreTokenElementApis.ColorTokenElementApiHelper;
import com.github.ruediste.jreact.utility.ColorPaletteTokenApis.Combined;
import com.github.ruediste.jreact.utility.UtilityElementApis;

public interface ThemeElementApis {

    public interface ElementThemeApiHelper<Api>
            extends UtilityElementApis.UtilityElementApiHelper<Api>,
            ColorTokenElementApiHelper<CombinedColorTokens<Api, CombinedColorTokensHelper<Api>>> {
    }

    public interface CombinedColorTokensHelper<Api>
            extends ColorHelper<Api>, ColorTokens.ColorValuesHelper {
    }

    public interface CombinedColorTokens<Api, Helper extends CombinedColorTokensHelper<Api>>
            extends ColorTokens<Api, Helper>, Combined<Api, Helper> {
    }

    public interface ElementThemeApiBase<Self, Helper extends ElementThemeApiHelper<Self>>
            extends UtilityElementApis.UtilityElementApiBase<Self, Helper>,
            ColorTokenElementApi<Self, CombinedColorTokens<Self, CombinedColorTokensHelper<Self>>, Helper> {

    }

    public interface ElementWithChildrenThemeApiBase<Self, //
            Helper extends ElementThemeApiHelper<Self> & CoreElementApis.ChildrenApiHelper<Self>>
            extends ElementThemeApiBase<Self, Helper>, CoreElementApis.ChildrenApi<Self, Helper> {

    }

}
