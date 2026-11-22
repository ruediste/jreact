package com.github.ruediste.jreact.utility;

import com.github.ruediste.jreact.CoreElementApis;
import com.github.ruediste.jreact.style.CoreTokenElementApis;

public interface UtilityElementApis {

    public interface UtilityElementApiHelper<Api>
            extends CoreElementApis.ElementApiHelper<Api>, UtilityTokenApis.FontUtilityApiHelper<Api>,
            CoreTokenElementApis.RadiusTokenApiHelper<UtilityTokenApis.RadiusTokenApi<Api>>,
            CoreTokenElementApis.SpacingTokenElementApiHelper {
    }

    public interface UtilityElementApiBase<Self, Helper extends UtilityElementApiHelper<Self>>
            extends CoreElementApis.ElementStyleApiBase<Self, Helper>,
            UtilityTokenApis.FontTokenElementApi<Self, Helper>,
            CoreTokenElementApis.RadiusTokenElementApi<Self, UtilityTokenApis.RadiusTokenApi<Self>, Helper>,
            CoreTokenElementApis.SpacingIntTokenElementApi<Self, Helper>,
            CoreTokenElementApis.SpacingFloatTokenElementApi<Self, Helper> {

    }

    public interface WithChildrenUtilityElementApiBase<Self, //
            Helper extends UtilityElementApiHelper<Self> & CoreElementApis.ChildrenApiHelper<Self>>
            extends UtilityElementApiBase<Self, Helper>, CoreElementApis.ChildrenApi<Self, Helper> {
    }

}
