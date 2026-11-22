# Remaining Work

This file tracks deferred implementation work and review findings. Design and
implementation details live in `doc/`.

- use the jreact icon from the storybook documentation for the java storybook project/window icon

## Accessing nodes
Add `var ref=useRef()` hook; Extend style with `ref(ref)`. `ref.current` contains the node after rendering. After layout,
position can be obtained from node.


## Menu/Popover/Modal Handling
The UI provides mechanisms to add remove nodes to the end of the root node.
`showModal(close->{})`. The lambda runs in the context of the root. Modals are added to the end and can be removed from the list.
Place using "display: absolute";

need to get width/height of popup component before display. use `useLayoutEffect`, measure node, adjust placement.

## Icons
Use https://kordamp.org/ikonli/ for icons

## Styling
Likely remove the style rules. Extend inline styling with selectors/predicates, to replicate Tailwind CSS functionality.

## Swing Integration
Move creation/update/destruction of Swing Components to the individual node class.

## Accordion Styling Implementation Plan

Goal: build the shadcn/Base UI accordion from inline styles plus selector variants.
No CSS files and no animations. Semantic colors stay in the theme layer with
shadcn/ui names; Tailwind-style scales and helpers stay in the utility layer.
This supersedes the earlier "Likely remove the style rules" note: inline rules are
the mechanism for selector-based variants.

### 0. Finish the existing selector-rule plumbing

- [x] `ElementApiHelperImpl.selectorTerminal` creates a `StyleRule` but never adds it
      to `node.styleRules`; append it so `JReactRoot.resolveStyles` evaluates it.
- [x] Preserve the initial factory in `selectorChained`, so `ancestor()` /
      `parent()` actually wrap the terminal selector.
- [x] Define variant scope. After `hover()` / `notLast()` all following style calls
      currently go into that rule; add `inline()` to return to inline declarations
- [x] Keep precedence simple and documented: selector rules in declaration order,
      then inline declarations win. Update the `CoreStyleApis` javadoc; specificity
      and component scope are not implemented today.
- [x] Add tests for rule registration, inline precedence, and chained
      `parent()` / `ancestor()` selectors.

### 1. Selector extensions needed by the accordion

- [x] Add `notLast()` (Tailwind `not-last:`) for the item bottom border. Add
      `first()` / `last()` only if another component needs them.
- [x] Add `focusVisible()` and an `ElementNode.focusVisible` flag. Clear it on pointer
      press, set it for keyboard focus. Until keyboard focus traversal exists, it can
      alias `focus()`; use `focusVisible()` in component code from the start.
- [x] Reuse the existing `hover()`, `active()`, `focus()`, and `disabled()` selectors.
- [x] Make `disabled()` usable: the accordion trigger must set `node.disabled`.
      Currently only `InputApi` sets it.

### 1.1 Style Inheritance
- [x] Extend `StyleProperties.Property` with an inherited flag, indicating if the style property should be inherited
- [x] mark the style properties with inheritance according to the CSS standard
- [x] extends style resolution with inheritance. This implies that style properties can only be resolved on a node. Resolution on a Style directly does not make any sense anymore.

### 2. Style properties

- [x] Add `TEXT_DECORATION` (`NONE`, `UNDERLINE`) as an inherited property, with
      `underline()` / `noUnderline()`, and paint underlines in `TextElement`.
      Property inheritance already handles `hover:underline` on a trigger reaching
      user-provided text; no separate ancestor resolution is needed.
- [x] Inherit the text properties (`COLOR`, `FONT_FAMILY`, `FONT_SIZE`, `BOLD`,
      `ITALIC`, `LINE_HEIGHT`), plus `TEXT_SHADOW`, `DIRECTION`, and `TEXT_ALIGN`.
      This was implemented with the `Property` inheritance flag in section 1.1.
- [x] Add `POINTER_EVENTS` (`AUTO`, `NONE`) and include it in hit testing so
      `pointer-events-none` disables interaction.
- [x] Add focus-ring properties: `RING_WIDTH`, `RING_COLOR`, `RING_OFFSET`. Paint the
      ring after the border, respecting border radius, in `ElementNode` / `PaintUtils`.
- [x] Add color-alpha support to make shadcn `ring/50` possible (for example
      `JColor.withAlpha()` and an alpha overload on theme color tokens).
- [x] Keep the Swing font limitation: normal/bold/italic only, no `font-medium`.
- [x] Do not add transitions, keyframes, or CSS variables yet.

### 3. Utility layer (Tailwind concepts)

- [x] Extend spacing tokens to `gap`, `gapX`, `gapY`, plus `size`, `wFull` / `hFull`,
      and auto margins (`mAuto`, `mlAuto`, ...).
- [x] Add flex helpers used by the accordion and similar components:
      `flex1()`, `grow()`, `grow0()`, `shrink()`, `shrink0()`.
- [x] Add border helpers: `border()` as the 1px default, plus
      `border0()` / `border2()` / `border4()` / `border8()`. Per-side methods
      already exist.
- [x] Add ring helpers (`ring()`, `ringWidth()`, `ringOffset()`); `underline()`,
      `noUnderline()`, and `pointerEventsNone()` already come from the core style API.
- [x] Add the missing element factories (`span`, `text`, `icon`, `input`, `select`)
      to `UtilityElements` and `ThemeElements`.
- [x] Keep semantic colors out of the utility layer; they belong to the theme.

### 4. Theme layer (shadcn/ui)

- [x] Keep the semantic token set aligned with shadcn/ui: `background`,
      `foreground`, `card`, `popover`, `primary`, `secondary`, `muted`, `accent`,
      `destructive`, `border`, `input`, `ring`, their foreground variants, and the
      chart/sidebar tokens. Chart and sidebar values are now part of
      `ColorTokens.Values` and exposed through `ColorTokens`.
- [x] Add fluent theme color helpers for every color-bearing property, at least
      `borderColor().border()` and `ringColor().ring()`, with alpha variants for
      `ring/50`. Per-side border color tokens are available too.
- [x] Make `ThemeElements` expose every element factory, not only `div()`.
- [x] Keep light/dark derivation in `ColorTokens.Values`; do not duplicate colors in
      utility tokens.

### 5. Elements

- [x] Add `span()`, `text(String)`, `icon(Icon)`, `input(...)`, and `select(...)` to
      `UtilityElements` and `ThemeElements`, mirroring `CoreElements`.
- [x] Containers (`div`, `span`) get the children API; `text`, `icon`, `input`, and
      `select` stay leaves.
- [x] Add core node-state helpers needed by primitives: `disabled(boolean)` and
      `focusable(boolean)`. Expanded/open is an accordion concept and stays in the
      primitive context, not on `ElementNode`.
- [x] Reduce duplication between `CoreElements`, `UtilityElements`, and
      `ThemeElements` where practical. The API implementations now share
      `CoreElements.ElementApiImplBase<Api, Helper>`.

### 6. Accordion primitive

- [x] Extend `ItemContext` with the item value, `isOpen`, `isDisabled`,
      `orientation`, `toggle`, `setOpen`, and an accessible name.
- [x] Expose the same state through `TriggerArgs` and `ContentArgs` via their
      `ItemContext`. The primitive calls the render callbacks; those callbacks apply
      the styles themselves, so no style callback is needed in the primitive `Args`.
- [x] Wire the root `onValueChange`, controlled and uncontrolled single mode, and
      collapsible behavior.
- [x] Apply `node.focusable`, `node.disabled`, and the accessible name in the
      component trigger render path; the primitive config handles click and
      Enter/Space. Expanded state stays in the accordion context, not on the node.
- [x] Keep the content mounted so child state survives toggling; the component
      applies `display(NONE)` when closed. No animation.
- [x] Add tests for open/close, collapsible behavior, disabled items, controlled
      mode, content mounting, and keyboard activation.

### 7. Accordion component styling

- [x] Root: `display(FLEX)`, `width(Dimension.percent(1f))`,
      `flexDirection(COLUMN)`.
- [x] Item: `notLast()` with a solid bottom border, using the theme `border` token.
- [x] Header: `display(FLEX)`.
- [x] Trigger: `flex1()`, `alignItems(START)`, `justifyContent(SPACE_BETWEEN)`,
      `borderRadius().lg()`, 1px transparent border, `paddingTop(2.5f)` /
      `paddingBottom(2.5f)`, `textAlign(LEGACY_LEFT)`, `textSm()`, `fontBold()`.
      Bold is used because Swing cannot express `font-medium`.
- [x] Trigger hover: accent background plus `hover().underline()`.
- [x] Trigger focus: `focusVisible().ringWidth(3)` with the `ring` token at 50%
      alpha. Trigger disabled: `opacity(0.5)` and `pointerEventsNone()`.
- [x] Chevron: branches on `isOpen`; 16px, `shrink0()`, `mlAuto()`, and
      `color().mutedForeground()`. No rotation animation.
- [x] Content: `display(isOpen ? FLEX : NONE)`, `overflow(HIDDEN)`,
      `paddingTop(0)`, `paddingBottom(2.5f)`, `textSm()`, column direction.
- [x] Add an accordion story to the storybook using the themed element APIs.

### 8. Verification and documentation

- [x] Unit tests for the style properties, selector predicates, and painting
      (underline, ring, ring offset, pointer events).
- [x] Accordion interaction and style integration tests.
- [x] Update `doc/src/content/docs/reference/primitives-and-css.mdx` for selector
      variants, new properties, inheritance, and the remaining limitations.
      The architecture reference style section was aligned with the current model.
- [x] Run `mvn test` and build the documentation site.

### Explicitly out of scope for this plan

- CSS files/strings, global stylesheets, and arbitrary `[&_...]` selectors.
- Generic `data-*` / `aria-*` attribute selectors; use typed node state instead.
- Transitions, keyframes, panel-height animation, and CSS custom properties.
- Font weights beyond normal/bold/italic.