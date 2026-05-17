---
name: Systematic Professional
colors:
  surface: '#f9faf5'
  surface-dim: '#d9dad6'
  surface-bright: '#f9faf5'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f3f4f0'
  surface-container: '#edeeea'
  surface-container-high: '#e8e8e4'
  surface-container-highest: '#e2e3df'
  on-surface: '#1a1c1a'
  on-surface-variant: '#45474d'
  inverse-surface: '#2f312e'
  inverse-on-surface: '#f0f1ed'
  outline: '#75777d'
  outline-variant: '#c5c6cd'
  surface-tint: '#545e76'
  primary: '#051125'
  on-primary: '#ffffff'
  primary-container: '#1b263b'
  on-primary-container: '#828da7'
  inverse-primary: '#bbc6e2'
  secondary: '#47607e'
  on-secondary: '#ffffff'
  secondary-container: '#c2dcff'
  on-secondary-container: '#48617e'
  tertiary: '#001225'
  on-tertiary: '#ffffff'
  tertiary-container: '#0f273f'
  on-tertiary-container: '#798fab'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#d7e2ff'
  primary-fixed-dim: '#bbc6e2'
  on-primary-fixed: '#101b30'
  on-primary-fixed-variant: '#3c475d'
  secondary-fixed: '#d1e4ff'
  secondary-fixed-dim: '#afc9ea'
  on-secondary-fixed: '#001d36'
  on-secondary-fixed-variant: '#2f4865'
  tertiary-fixed: '#d1e4ff'
  tertiary-fixed-dim: '#b2c8e7'
  on-tertiary-fixed: '#031d34'
  on-tertiary-fixed-variant: '#334861'
  background: '#f9faf5'
  on-background: '#1a1c1a'
  surface-variant: '#e2e3df'
typography:
  display-lg:
    fontFamily: Inter
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
    letterSpacing: -0.02em
  display-lg-mobile:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '700'
    lineHeight: 32px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
    letterSpacing: -0.01em
  headline-sm:
    fontFamily: Inter
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  label-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '500'
    lineHeight: 20px
  label-sm:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.05em
rounded:
  sm: 0.125rem
  DEFAULT: 0.25rem
  md: 0.375rem
  lg: 0.5rem
  xl: 0.75rem
  full: 9999px
spacing:
  base: 4px
  xs: 4px
  sm: 8px
  md: 16px
  lg: 24px
  xl: 32px
  gutter: 24px
  margin-mobile: 16px
  margin-desktop: 40px
  max-width: 1280px
---

## Brand & Style
This design system is engineered for high-utility contact management, prioritizing clarity, efficiency, and a sense of calm organization. The target audience includes professionals and administrative teams who require a tool that feels reliable and stays out of the way of their primary tasks.

The visual style is **Corporate Minimalism**. It avoids decorative flourishes in favor of structured data presentation and purposeful white space. By utilizing a restrained color palette and precise alignment, the UI evokes an emotional response of competence and control. The aesthetic focuses on "utility-first" design, ensuring that every element serves a functional purpose while maintaining a sophisticated, modern professional atmosphere.

## Colors
The palette is built on a foundation of professional confidence. 

- **Primary (#1B263B):** Deep Navy. Used for high-emphasis actions, navigation backgrounds, and primary branding elements.
- **Secondary (#415A77):** Slate Gray. Used for sub-headers, secondary icons, and interactive states that require distinction without high-contrast competition.
- **Tertiary (#778DA9):** Muted Blue-Gray. Utilized for borders, inactive states, and placeholder text.
- **Neutral (#E0E1DD):** Clean White-Gray. Used for surface dividers, subtle background fills, and hover states on light elements.

The background is a soft off-white to reduce eye strain, while cards and primary containers use pure white to pop against the background.

## Typography
Inter is used across all levels for its exceptional legibility and systematic appearance. 

- **Headlines:** Use tighter letter-spacing and heavier weights to establish clear content sections.
- **Body Text:** Standard weight (400) is used for all contact information and general reading to maximize clarity.
- **Labels:** Small labels use an uppercase transform with slight letter spacing to differentiate them from data entries.

The system uses a rhythmic scale where the primary body size is 14px, optimized for data-dense environments like contact lists and CRM tables.

## Layout & Spacing
The layout follows a **Fixed Grid** model for desktop, centering the content at a maximum width of 1280px to ensure line lengths remain readable. 

- **Grid:** A 12-column system is used for dashboard views and contact profiles. 
- **Rhythm:** An 8px linear scale governs all padding and margins. 
- **Adaptation:** On mobile, the grid collapses to a single column with 16px side margins. Tablets utilize a 24px gutter and a 6-column grid.

Generous padding is applied within contact cards and list items (24px) to prevent the professional UI from feeling cramped or overwhelming during heavy usage.

## Elevation & Depth
This design system utilizes **Low-contrast outlines** combined with **Tonal layers** rather than heavy shadows.

- **Level 0 (Background):** #F8F9FA.
- **Level 1 (Cards/Surface):** #FFFFFF with a 1px solid border of #E0E1DD.
- **Level 2 (Dropdowns/Modals):** #FFFFFF with a very soft, subtle shadow (0px 4px 12px rgba(27, 38, 59, 0.05)) to distinguish overlapping elements.

Depth is communicated through subtle shifts in background color and clean, hairline borders. This approach keeps the interface feeling "flat" and modern while providing enough tactile feedback to understand the hierarchy of information.

## Shapes
The shape language is **Soft (0.25rem)**. This subtle rounding removes the "sharpness" of a purely utilitarian tool without making it appear overly casual or "bubbly."

- **Standard Elements (Buttons, Inputs):** 4px (0.25rem) radius.
- **Large Elements (Cards, Modals):** 8px (0.5rem) radius.
- **Avatars:** Circular (50%) to contrast with the predominantly rectangular grid.

## Components
- **Buttons:** Primary buttons use a solid #1B263B background with white text. Secondary buttons use a #E0E1DD background with #1B263B text. Hover states involve a slight darkening of the background.
- **Input Fields:** Fields are outlined with #E0E1DD. On focus, the border changes to #415A77 with a subtle 2px outer glow of the same color at 10% opacity.
- **Contact Lists:** Row-based layouts with subtle #E0E1DD bottom borders. Hovering over a row triggers a #F8F9FA background fill.
- **Chips:** Used for tagging contacts. These have a #F1F2F0 background, #415A77 text, and no border.
- **Data Tables:** High-density headers with #1B263B text and 1px bottom borders. Vertical cell lines are omitted to maintain horizontal flow.
- **Avatars:** Each contact record should feature a circular avatar. If no image is present, use a #778DA9 background with white initials in `label-md`.