package com.github.ruediste.jreact;

import java.util.Objects;
import java.util.function.Consumer;

import com.github.ruediste.jreact.elements.ElementNode;
import com.github.ruediste.jreact.events.ClickEvent;
import com.github.ruediste.jreact.style.ApiHelperProvider;
import com.github.ruediste.jreact.style.CoreStyleApis;
import com.github.ruediste.jreact.style.CoreStyleApis.LayoutStyleApi;
import com.github.ruediste.jreact.style.CoreStyleApis.StyleApiHelper;

public class CoreElementApis {
    public interface ChildrenApiHelper<Api> {
        Api withChildren(Runnable children);
    }

    public interface ChildrenApi<Self, Helper extends ChildrenApiHelper<Self>> extends ApiHelperProvider<Helper> {
        default Self children(Runnable children) {
            return _helper().withChildren(children);
        }
    }

    public interface NodeAccessApiHelper<Api> {
        Api apply(Consumer<ElementNode> updater);
    }

    public interface ElementNodeCoreApi<Self, Helper extends NodeAccessApiHelper<Self>>
            extends ApiHelperProvider<Helper> {
        default Self key(Object key) {
            return _helper().apply(n -> n.key = key);
        }

        default Self id(String id) {
            return _helper().apply(n -> n.id = id);
        }

        default Self classNames(String... classNames) {
            Objects.requireNonNull(classNames);
            return _helper().apply(n -> {
                n.classNames.clear();
                for (String className : classNames)
                    n.classNames.add(Objects.requireNonNull(className));
            });
        }

        default Self className(String className) {
            Objects.requireNonNull(className);
            return _helper().apply(n -> n.classNames.add(className));
        }

        default Self accessibleName(String accessibleName) {
            return _helper().apply(n -> n.accessibleName = accessibleName);
        }

        @SuppressWarnings("unchecked")
        default Self apply(Consumer<? super Self> consumer) {
            return _helper().apply(n -> consumer.accept((Self) this));
        }
    }

    public interface EventApiHelper<Api> {
        <T extends JReactEvent> Api on(Class<T> eventClass, EventListener<T> listener);
    }

    public interface EventApi<Self, Helper extends EventApiHelper<Self>> extends ApiHelperProvider<Helper> {
        default Self onClick(EventListener<ClickEvent> onClick) {
            return _helper().on(ClickEvent.class, onClick);
        }
    }

    public interface ElementApiHelper<Api> extends StyleApiHelper<Api>, ChildrenApiHelper<Api>, EventApiHelper<Api>,
            NodeAccessApiHelper<Api> {
    }

    public static interface ElementStyleApiBase<Self, Helper extends ElementApiHelper<Self>>
            extends ElementNodeCoreApi<Self, Helper>,
            LayoutStyleApi<Self, Helper>,
            CoreStyleApis.BorderStyleApi<Self, Helper>,
            CoreStyleApis.ColorStyleApi<Self, Helper>,
            CoreStyleApis.FontStyleApi<Self, Helper>,
            CoreStyleApis.ShadowStyleApi<Self, Helper>,
            CoreStyleApis.OpacityStyleApi<Self, Helper>,
            EventApi<Self, Helper> {
    }

}
