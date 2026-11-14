# Notes
This file contain various notes. It is used to dump ideas before organizing them.

## DSL Design
- static methods
  - convenient to use
  - cannot combine and extend
  - not flexible
- interfaces
  - multiple inheritance provides flexibility
  - always need an instance to use it
    - callback, inject instance
    - static use member on some class, copy to local scope `var ui=Ui.instance; ui.div();`
  - base class
    - basic idea: create base class `Ui` with the stuff you need, then `class MyPage extends Ui { ... div()...}`
    - downside basic idea: `Ui` is composed from interfaces, these methods remain public, thus `MyPage` has all the utility members
    - workaround: `class MyPage extends Ui { C c=new C(); class C { void MyComponent(){... div() ...} }; }`; Usage: `MyPage.c.MyComponent()`
- fluent interfaces
  - nicely scope available options to exactly the context where they are needed
  - solve the issue of bringing the options into scope
  - good for flat structures, nesting is hard to express

On style:
- layout stuff is passed to Taffy. Pretty much fixed, dictated by framework/taffy
- other styling can be application specific
- typical CSS stuff and associated helpers for rendering are provided by framework, but not required
- style is already a map with typed keys, easy to extend

On Selectors
- there are only inline styles and inline rules, no need for traditional CSS rules
- with inline rules, we do not need selector weights, order of rules is sufficient
- Selector is not the perfect word for what we need, predicate or test would be more precise. But stick to selector, as it is a well known term.

On style recalculation
- hover, active etc can change without any rendering
- rendering can change other parts of the tree
- nodes provide events for rerender and change of hover/active etc
- selectors return match/no match and callback to register a selector with all relevant events
- combining selectors (and(), or(), ancestors(), ...) combine the registration callbacks of all inner selectors
- when selector is evaluated on node, it is registered with events. When events fire, node is registered for re-painting or re-layout
- First version: mechanism not needed, can implement to improve performance

## Primitive API
Likely a fluent API for style and other config is a good idea.
```java
div.displayFlex();
icon().home().marginLeft(12);
```

## Selectors from Context

```java
ui.div().border(12).rule(ancestor(hover())).background().gray().children(()->{
    ...
});
```

Can express complex selectors. Pollution.

## Fluent Selectors
```java
ui.div().border(12).ancestor().hover().background().gray().active().background().red().children(()->{
    ...
});
```

Good as long as selectors only take one argument. In most cases, that is likely all that's needed. Can use `and` and `or`, as long as structure is `(((A) and B) or C) and D`. Not possible: `((A) and B) or (C and D)`. But this could be done with selectors from context. `ancestor.hover().and(Selectors.nextSibling().active().or().prevSibling.active())`

## Applying Theme to Style API
It is desirable to integrate the theme into the style API. For example: `div().marginTop().m().backgroundColor().accent()` or `div().marginTopM()`. Combining the all size/color options with the style attributes can either be done with a separate method call, which can be achieved with a few generic parameters. The second form is more copy-paste work, or could be achieved with an annotation processor to generate the code. Or with a standalone code generator, which is a bit more awkward to use, but easier to setup/integrate with IDEs. And typically, the code has to be generated once and does not change often afterwards.