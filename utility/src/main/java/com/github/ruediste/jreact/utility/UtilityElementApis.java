package com.github.ruediste.jreact.utility;

import com.github.ruediste.jreact.CoreElementApis;
import com.github.ruediste.jreact.style.CoreTokenApis;

public interface UtilityElementApis {

    public interface ElementUtilityApiHelper<Api>
            extends CoreElementApis.ElementApiHelper<Api>, UtilityTokenApis.FontUtilityApiHelper<Api>,
            CoreTokenApis.RadiusTokenApiHelper<UtilityTokenApis.RadiusApi<Api>> {
    }

    public interface ElementUtilityApiBase<Self, Helper extends ElementUtilityApiHelper<Self>>
            extends CoreElementApis.ElementStyleApiBase<Self, Helper>,
            UtilityTokenApis.FontUtilityApi<Self, Helper>,
            CoreTokenApis.RadiusTokenApi<Self, UtilityTokenApis.RadiusApi<Self>, Helper> {

    }

    public interface ElementWithChildrenUtilityApiBase<Self, //
            Helper extends ElementUtilityApiHelper<Self> & CoreElementApis.ChildrenApiHelper<Self>>
            extends ElementUtilityApiBase<Self, Helper>, CoreElementApis.ChildrenApi<Self, Helper> {
    }
}
