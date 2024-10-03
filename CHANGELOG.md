<!-- Copyright 2024 Léo de Souza -->
<!-- SPDX-License-Identifier: Apache-2.0 -->

## 2.0.3 – Unreleased

### Bug Fix

- Fix bug where time could exceed ten hours and take too much space

### Improvements

- Do not color text in red when there is still less than 100 ms left
- Increase precision by not stopping the clock when switching players
- Update dependencies

### Note

- Minor formatting improvements

## [2.0.2](https://github.com/ldeso/blitz/releases/tag/v2.0.2) – 2024-09-01

This release updates dependencies.

### Improvement

- Update dependencies

### Notes

- Do not use manual execution control in local tests
- Fix anchor links in CHANGELOG.md
- Improve app description
- Use delegated property to access release signingConfig
- Move delay after animation back to `AnimatedBackHandler`
- Use static badges for Accrescent and Google Play versions
- Add comment about origin of app icon in REUSE.toml
- Minor formatting improvements

## [2.0.1](https://github.com/ldeso/blitz/releases/tag/v2.0.1) – 2024-06-19

This release fixes a bug introduced in version [2.0.0](#200--2024-06-19) where the clock would not pause properly if the time ended during a back gesture.

### Bug Fix

- Fix bug where clock would reset if time ended during back gesture

### Note

- Switch links to HTTPS in README.md

## [2.0.0](https://github.com/ldeso/blitz/releases/tag/v2.0.0) – 2024-06-19

This release targets Android 15, removes unnecessary backup and restore functionality and updates dependencies. The app is now considered feature-complete. 

### Improvements

- Target Android 15
- Disable backup and restore
- Update dependencies

### Notes

- Create `ScopedEffectHandler` component
- Create io package
- Move back handlers to the components package
- Minor code simplifications
- Add Obtainium badge
- Update Gradle wrapper
- Add toolchain resolver plugin
- Remove redundant `android.nonTransitiveRClass=true`
- Trust dependency signing keys globally
- Improve app description

## [1.9.1](https://github.com/ldeso/blitz/releases/tag/v1.9.1) – 2024-05-31

This release enables [dependency verification](https://docs.gradle.org/current/userguide/dependency_verification.html) in Gradle and updates dependencies.

### Improvements

- Enable dependency verification
- Update dependencies

### Notes

- Use Gradle task configuration avoidance API
- Improve app description

## [1.9.0](https://github.com/ldeso/blitz/releases/tag/v1.9.0) – 2024-05-24

This release adds an audible feedback when players touch the screen at the end of their turn, which becomes a haptic feedback if the ringtone is set to vibrate.

### New Feature

- Provide audible or haptic feedback on click

### Improvement

- Update dependencies

### Notes

- Do not set redundant `layoutInDisplayCutoutMode`
- Improve app description

## [1.8.8](https://github.com/ldeso/blitz/releases/tag/v1.8.8) – 2024-05-21

This release improves the style of the text, enables hardware memory tagging on compatible devices and updates dependencies.

### Improvements

- Use tabular numerals instead of a monospaced font
- Adapt text size to default device font
- Enable memory tagging in asynchronous mode
- Update dependencies

### Notes

- Simplify declaration of signingConfigs
- Use `includeGroupAndSubgroups` instead of `includeGroupByRegex`
- Add comments to gradle.properties
- Fix incorrect element in README.md
- Fix missing space in French title.txt
- Apply auto-formatting

## [1.8.7](https://github.com/ldeso/blitz/releases/tag/v1.8.7) – 2024-05-13

This release fixes two bugs triggered by back actions and slightly simplifies the codebase.

### Bug Fixes

- Block input during predictive back gesture animation
- Allow leaving the app with a single back action on pause

### Notes

- Move save and restore logic to ViewModel
- Clarify app signature in README.md

## [1.8.6](https://github.com/ldeso/blitz/releases/tag/v1.8.6) – 2024-05-09

This release adds Blitz to the Accrescent store and removes the encrypted Dependency Info Block from the signature block of the APK, making the build truly reroducible and resulting in an APK without any proprietary blob.

### Improvement

- Remove encrypted Dependency Info Block

### Notes

- Sign release builds from keystore.properties file
- Generate APK sets with bundletool
- Add Accrescent to README.md

## [1.8.5](https://github.com/ldeso/blitz/releases/tag/v1.8.5) – 2024-05-02

This release updates dependencies.

### Improvement

- Update dependencies

### Notes

- Use `PluginDependenciesSpec.kotlin` method for Kotlin Android plugin
- Add non-breaking spaces in CHANGELOG.md

## [1.8.4](https://github.com/ldeso/blitz/releases/tag/v1.8.4) – 2024-04-30

This release fully enables edge-to-edge in anticipation of the changes that will be introduced in Android 15.

### Improvement

- Consider display cutout areas on the long edges of the screen

### Note

- Rename gradle plugins

## [1.8.3](https://github.com/ldeso/blitz/releases/tag/v1.8.3) – 2024-04-29

This release changes the font size according to the size of system bars and display cutouts, and updates licensing and copyright information to the version 3.2 of the REUSE specification.

### Improvements

- Consider system bars and display cutouts when calculating font size
- Follow REUSE specification, version 3.2

### Notes

- Use "debug" signing config instead of creating a new one
- Remove unnecessary lines from .gitignore
- Remove Compose previews
- Increase size of screenshots in README.md
- Use highest resolution app icon in README.md

## [1.8.2](https://github.com/ldeso/blitz/releases/tag/v1.8.2) – 2024-04-19

This release adds licensing and copyright information to every file according to the version 3.0 of the REUSE specification, extends "personal data" to "any user data" in the privacy policy, and updates dependencies.

### Improvements

- Follow REUSE specification, version 3.0
- Use more restrictive privacy policy
- Update dependencies

### Notes

- Add `@PreviewScreenSizes` annotation
- Update website theme
- Do not hardcode application ID in manifest
- Remove unnecessary dependency
- Clarify debian/copyright file
- Restore title in README.md
- Improve icons in README.md

## [1.8.1](https://github.com/ldeso/blitz/releases/tag/v1.8.1) – 2024-04-11

This release enables APK Signature Schemes v3 and v4, updates dependencies and adds local tests.

### Improvements

- Enable APK Signature Schemes v3 and v4
- Update dependencies

### Notes

- Use Kotlin Duration and TimeSource
- Add local tests
- Switch to single-project build
- Round time down to nearest tenth of second
- Add `currentTime` variable to decrease code repetitions
- Rename `CallbackCaller` to `CallbackHandler`
- Use correct default values in `ClockContentPreview`
- Add parameter names in restoreSaved… signatures
- Improve format of CHANGELOG.md
- Add missing `alt` attribute in README.md
- Remove trailing newline in all metadata files

## [1.8.0](https://github.com/ldeso/blitz/releases/tag/v1.8.0) – 2024-04-02

This release is a large refactoring that moves the ticking logic from the UI layer to the ViewModel. It also restores the behavior where the app is closed on the first back event if the configuration is set to its default value, and switches back to a round legacy icon.

### Improvements

- Do not reset configuration if it is back to defaults
- Use round legacy icon

### Notes

- Use more enums to simplify code
- Move ticking logic from `ClockScreen` to `ClockViewModel`
- Stop and restart ticking when switching players
- Use `LifecycleStartEffect` instead combining a `DisposableEffect` with a `LifecycleObserver`
- Rename ClockInput.kt to ClockInputs.kt
- Add parameter name in `onOrientationChanged` signature
- Replace `if` with `when` in `LeaningSideHandler`
- Clarify `LeaningSideHandler` documentation
- Pass `displayOrientation` instead of `isLandscape`
- Remove unused import
- Move source files directly under the source root
- Format non-source files

## [1.7.8](https://github.com/ldeso/blitz/releases/tag/v1.7.8) – 2024-03-28

This release fixes two bugs that could be triggered by back gestures when the time was close to zero.

### Bug Fixes

- Fix bug where clock would be stuck if time ended during back gesture
- Fix bug where the first time save could be off by one minute/second

## [1.7.7](https://github.com/ldeso/blitz/releases/tag/v1.7.7) – 2024-03-28

This release fixes a bug reintroduced in version [1.7.1](#171--2024-03-26) where the wrong animation was shown if the time ended during a back gesture.

### Bug Fix

- Fix bug where wrong animation was shown if time ended during back gesture

### Note

- Rename `nextTurn` to `play`

## [1.7.6](https://github.com/ldeso/blitz/releases/tag/v1.7.6) – 2024-03-28

This release makes the time of each player independent from the UI state, which results in less frequent recompositions and an increased responsiveness.

### Improvement

- Separate time from UI state to increase responsiveness

### Notes

- Rename source directory from java to kotlin
- Rename "ChessClock" to "Clock"
- Move `ClockBackHandler` to ClockInput.kt
- Create LeaningSideHandler
- Add favicon.ico
- Update "About" section in README.md

## [1.7.5](https://github.com/ldeso/blitz/releases/tag/v1.7.5) – 2024-03-27

This release disables back gesture animations below Android 14.

### Improvement

- Do not animate back gestures below Android 14

### Notes

- Fix typo in names of variables
- Remember onOrientationChanged with rememberUpdatedState
- Do not use lazy to create orientationEventListener
- Move calculation of rotation out of DisposableEffect

## [1.7.4](https://github.com/ldeso/blitz/releases/tag/v1.7.4) – 2024-03-26

This release fixes a bug introduced in version [1.7.3](#173--2024-03-26) where the time was displayed upside down.

### Bug Fix

- Fix bug where time was displayed upside down

## [1.7.3](https://github.com/ldeso/blitz/releases/tag/v1.7.3) – 2024-03-26

This release restores a feature that was removed in version [1.7.2](#172--2024-03-26) where the orientation of the device must cross a threshold of 10 degrees to be able to change the display orientation.

### Bug Fix

- Restore threshold of 10 degrees before changing display orientation

### Note

- Add whitespace

## [1.7.2](https://github.com/ldeso/blitz/releases/tag/v1.7.2) – 2024-03-26

This release fixes a long-standing bug where the time could be updated in the wrong direction due to rounding.

### Bug Fix

- Fix bug where time could update in the wrong direction due to rounding

### Notes

- Simplify dependencies
- Create component OrientationHandler
- Add missing documentation for `dragAmount`

## [1.7.1](https://github.com/ldeso/blitz/releases/tag/v1.7.1) – 2024-03-26

This release improves the experience for versions of Android below Android 14 by adding a default value for the starting side of back gestures, which depends on whether the layout is set from left to right or from right to left.

### Improvement

- Provide correct default value for swipe side of back gestures for API < 34

### Notes

- Add "About" section to README.md
- Simplify chessClockInput
- Remove redundant state

## [1.7.0](https://github.com/ldeso/blitz/releases/tag/v1.7.0) – 2024-03-25

This release adds a feature that animates the time of the current player when the clock is paused.

### New Feature

- Add animation to the time of the current player when the clock is paused

### Notes

- Add ChessClockInput.kt
- Simplify BasicTime
- Simplify ChessClockTickingEffect
- Simplify ChessClockBackHandler
- Simplify restoreSavedTime
- Simplify IsLeaningRightHandler
- Rename clock to chessClockViewModel
- Improve links in CHANGELOG.md

## [1.6.5](https://github.com/ldeso/blitz/releases/tag/v1.6.5) – 2024-03-24

This release fixes a bug introduced in version [1.6.3](#163--2024-03-24) where the time could change before the end of the predictive back gesture animation.

### Bug Fix

- Fix bug where time could change before end of predictive back gesture animation

## [1.6.4](https://github.com/ldeso/blitz/releases/tag/v1.6.4) – 2024-03-24

This release fixes a long-stanging bug where the clock would sometimes display negative time, as well as a bug introduced in version [1.6.0](#160--2024-03-24) where the position of the time would not reset when canceling a predictive back gesture.

### Bug Fixes

- Fix bug where a negative time would be displayed if the clock reached zero in the background
- Fix bug where position would not reset when canceling back gestures

## [1.6.3](https://github.com/ldeso/blitz/releases/tag/v1.6.3) – 2024-03-24

This release adds a delay to improve the predictive back gesture animation.

### Improvement

- Add delay to improve predictive back gesture animation

### Note

- Fix wrong release dates in CHANGELOG.md

## [1.6.2](https://github.com/ldeso/blitz/releases/tag/v1.6.2) – 2024-03-24

This release fixes a bug introduced in version [1.6.0](#160--2024-03-24) where dragging to change the time would give unexpected values.

### Bug Fix

- Fix a bug where dragging to change the time would yield incorrect values

## [1.6.1](https://github.com/ldeso/blitz/releases/tag/v1.6.1) – 2024-03-24

This release fixes two bugs introduced in version [1.6.0](#160--2024-03-24) that were triggered by predictive back gestures and the clock reaching zero.

### Bug Fixes

- Fix bug where wrong animation was shown if time ended during back gesture
- Fix bug where time would not change color when reaching zero

## [1.6.0](https://github.com/ldeso/blitz/releases/tag/v1.6.0) – 2024-03-24

This release introduces predictive back gestures within the app and drastically reduces the number of recompositions by hoisting state the closest to where it is consumed.

### New Feature

- Enable predictive back gestures

### Improvement

- Drastically reduce recompositions

### Note

- Add parameter names

## [1.5.8](https://github.com/ldeso/blitz/releases/tag/v1.5.8) – 2024-03-22

This release allows the app to conserve app resources using the collectAsStateWithLifecycle API to collect the uiState in a lifecycle-aware manner.

### Improvement

- Reduce app resources using collectAsStateWithLifecycle

### Notes

- Simplify code for adjusting the time and configuration
- Do not pass callbacks to ViewModel
- Simplify code to reset configuration
- Call `super.onCreate` before `enableEdgeToEdge`
- Use single .gitignore

## [1.5.7](https://github.com/ldeso/blitz/releases/tag/v1.5.7) – 2024-03-22

This release implements the official recommendation to use a LifecycleObserver in Compose instead of overriding lifecycle methods in the MainActivity.

### Improvement

- Do not override lifecycle methods in MainActivitiy

### Notes

- Simplify addMinutes and addSeconds methods
- Remove redundant documentation

## [1.5.6](https://github.com/ldeso/blitz/releases/tag/v1.5.6) – 2024-03-22

This release removes the permission DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION because it is unnecessary as the only broadcast receiver used in the app is the ProfileInstallReceiver, whose only purpose is to install the baseline profile used to compile some parts of the app ahead of time during installation.

### Improvements

- Remove unnecessary permission
- Update dependency

### Note

- Minor changes to README.md

## [1.5.5](https://github.com/ldeso/blitz/releases/tag/v1.5.5) – 2024-03-21

This release updates dependencies and refactors the code to hold the state and logic in a ViewModel instead of a plain class.

### Improvement

- Update dependencies

### Notes

- Hold the state and logic in a ViewModel
- Clarify that all sources use the same app signing key
- Capitalize title

## [1.5.4](https://github.com/ldeso/blitz/releases/tag/v1.5.4) – 2024-03-14

This release fixes a bug introduced in version [1.5.3](#153--2024-03-14) where per-app languages preferences were not supported anymore.

### Bug Fix

- Restore per-app language support

### Notes

- Use version catalog for JDK and Compose compiler
- Align screenshots vertically
- Remove whitespace

## [1.5.3](https://github.com/ldeso/blitz/releases/tag/v1.5.3) – 2024-03-14

This release updates Compose dependencies and introduces the website https://blitz.leodesouza.net built with GitHub Pages.

### Improvement

- Update dependencies

### Notes

- Set up https://blitz.leodesouza.net with GitHub Pages
- Add Apache License 2.0 boilerplate notice
- Add "Install" section to README.md
- Add `alt` attribute to images in README.md

## [1.5.2](https://github.com/ldeso/blitz/releases/tag/v1.5.2) – 2024-03-12

This release simplifies the codebase and updates metadata.

### Improvement

- Store state directly in chess clock model

### Notes

- Add links to README.md
- Explicitly convert from Long to Float
- Remove trailing newline in full description to avoid `<br>` insertion
- Update metadata images
- Simplify saving current time or duration/increment
- Do not use an interface with a single concrete class

## [1.5.1](https://github.com/ldeso/blitz/releases/tag/v1.5.1) – 2024-03-11

This release fixes a bug introduced in version [1.5.0](#150--2024-03-11) where changing the language would change the default duration and time increment.

### Bug Fix

- Fix bug where changing the language would change the default duration and increment

## [1.5.0](https://github.com/ldeso/blitz/releases/tag/v1.5.0) – 2024-03-11

This release is a big refactor of the code that tries to follow the principle of separation of concerns, and introduces a better support of the orientation on all devices.

### New Feature

- Properly handle orientation on all devices

### Notes

- Separate chess clock model, view and controller
- Change order of declarations
- Use callbacks instead of passing Window to composable

## [1.4.9](https://github.com/ldeso/blitz/releases/tag/v1.4.9) – 2024-03-06

This release reverts the behavior of rounding up to nearest second when above one hour that was introduced in version [1.4.7](#147--2024-03-06).

### Improvement

- Revert rounding up to nearest second above one hour

## [1.4.8](https://github.com/ldeso/blitz/releases/tag/v1.4.8) – 2024-03-06

This release fixes a bug introduced in the last release where, above one hour, the displayed time would sometimes be off by up to one second.

### Bug Fix

- Fix wrong time displayed above one hour

## [1.4.7](https://github.com/ldeso/blitz/releases/tag/v1.4.7) – 2024-03-06

This release improves the precision of the counter and of the time display.

### Improvement

- Round up to show the current (thenth of a) second

### Bug Fix

- Update current time on pause

## [1.4.6](https://github.com/ldeso/blitz/releases/tag/v1.4.6) – 2024-03-06

This release is another refactor that makes triggering the next turn more consistent between tapping and dragging actions.

### Bug Fix

- Trigger next turn from the end of a drag action instead of its start

### Notes

- Refactor chess clock policy into an interface
- Pass an immutable orientation state to its consumers

## [1.4.5](https://github.com/ldeso/blitz/releases/tag/v1.4.5) – 2024-03-05

This release vastly increases the sensitivity to touch events when counting down.

### Improvement

- Also trigger next turn from drag events

### Note

- Use application ID in build filename

## [1.4.4](https://github.com/ldeso/blitz/releases/tag/v1.4.4) – 2024-03-05

This release increases the precision and readability of the clock.

### Improvement

- Use a bold font to increase readability

### Bug Fix

- Fix a bug that skipped the last tenth of a second

## [1.4.3](https://github.com/ldeso/blitz/releases/tag/v1.4.3) – 2024-03-04

This release is a big refactor of the code handling dragging events, fixing two issues and improving the dragging experience.

### Improvement

- Improve single player's time settings ([#3](https://github.com/ldeso/blitz/issues/3))

### Bug Fixes

- Use monospace font to prevent digits from jumping ([#2](https://github.com/ldeso/blitz/issues/2))
- Fix D-Pad events being counted twice

### Notes

- Do not repeat short description in full description
- Add missing documentation

## [1.4.2](https://github.com/ldeso/blitz/releases/tag/v1.4.2) – 2024-02-29

This release restores the orientation sensor when using reverse portrait mode while avoiding issues when the orientation is locked.

### Improvement

- Restore orientation sensor when in reverse portrait

## [1.4.1](https://github.com/ldeso/blitz/releases/tag/v1.4.1) – 2024-02-29

This release fixes possible wrong orientations by disabling the orientation sensor when using reverse portrait mode.

### Bug Fix

- Disable orientation sensor when in reverse portrait

### Note

- Add icon and badges to README.md

## [1.4.0](https://github.com/ldeso/blitz/releases/tag/v1.4.0) – 2024-02-29

This release adds support for landscape mode and for devices without a touchscreen.

### New Features

- Add true landscape support
- Add support for devices with only a D-pad controller

### Improvement

- Enable per-app language support

### Note

- Add documentation for isBlackRightHanded

## [1.3.2](https://github.com/ldeso/blitz/releases/tag/v1.3.2) – 2024-02-29

This release fixes a bug where, when correcting a player’s time, the drag orientation would not flip based on the orientation of the clock.

### Bug Fix

- Fix frozen drag orientation when correcting time

## [1.3.1](https://github.com/ldeso/blitz/releases/tag/v1.3.1) – 2024-02-29

This release fixes a bug where the time could not be restarted after reaching zero.

### Bug Fix

- Fix bug where time could not be restarted after ending

## [1.3.0](https://github.com/ldeso/blitz/releases/tag/v1.3.0) – 2024-02-28

This release allows flipping the clock orientation for left-handed players and flips horizontal dragging for a right-to-left layout direction.

### New Feature

- Flip clock based on orientation for left-handed players

### Improvements

- Support right-to-left layout direction
- Add support for the predictive back gesture

### Bug Fix

- Make state survive activity or process recreation

### Notes

- Add privacy_policy.txt
- Add featureGraphic.png
- Add short description in title
- Shorten changelogs in metadata
- Shorten changelogs even more
- Set screen orientation in AndroidManifest.xml
- Improve code clarity by adding variables and imports
- Give default values to arguments of ChessClock
- Make BasicTime more basic
- Update documentation
- Replace ' with ’
- Replace - with *
- Minor code formatting changes
- Formatting

## [1.2.0](https://github.com/ldeso/blitz/releases/tag/v1.2.0) – 2024-02-25

This releases keeps the screen turned on and increases time precision by tracking system time instead of recompositions.

### New Feature

- Keep screen on during countdown

### Improvement

- Increase precision by updating time independently from refresh rate

### Notes

- Update dependencies
- Update JVM bytecode target version
- Update Gradle wrapper
- Remove themes.xml
- Remove tools:targetApi="31" from manifest
- Add documentation
- Change order of function definitions
- Apply auto-formatting
- Make changelogs valid markdown
- Fix link to last version

## [1.1.3](https://github.com/ldeso/blitz/releases/tag/v1.1.3) – 2024-02-22

This release adds metadata and slightly simplifies the source code.

### Improvements

- Add metadata
- Remove an if block to simplify code
- Further simplify if block

## [1.1.2](https://github.com/ldeso/blitz/releases/tag/v1.1.2) – 2024-02-22

This release improves compatibility and fixes a bug preventing a reset when both times are equal to their initial values.

### Bug Fix

- Allow reset when times are equal to their initial values

### Improvements

- Set minimum SDK version to 21 (Android 5.0)
- Enable Gradle configuration cache

## [1.1.1](https://github.com/ldeso/blitz/releases/tag/v1.1.1) – 2024-02-22

This release prevents adjusting the time after it reaches zero and reduces horizontal dragging sensitivity.

### Bug Fixes

- Disallow time adjustments when a time is up
- Reduce horizontal dragging sensitivity

### Note

- Slightly change code structure

## [1.1.0](https://github.com/ldeso/blitz/releases/tag/v1.1.0) – 2024-02-21

This release adds the possibility to select the time period using vertical dragging and the time increment using horizontal dragging.

### New Feature

- Implement time input by dragging

## [1.0.7](https://github.com/ldeso/blitz/releases/tag/v1.0.7) – 2024-02-21

This release updates and reduces dependencies, bringing the size of the APK down to 686 KiB.

### Improvements

- Use Compose Foundation instead of Material 3
- Update dependencies

## [1.0.6](https://github.com/ldeso/blitz/releases/tag/v1.0.6) – 2024-02-21

This release fixes a bug where it would not be possible to reset if one of the timers did not start.

### Bug Fix

- Allow a reset when one of the timers did not start

### Note

- Use 0L (Long) instead of 0 (Int)

## [1.0.5](https://github.com/ldeso/blitz/releases/tag/v1.0.5) – 2024-02-21

This release fixes some bugs introduced with the reset functionality.

### Bug Fixes

- Make it possible to leave the app
- Set first player to white on reset

## [1.0.4](https://github.com/ldeso/blitz/releases/tag/v1.0.4) – 2024-02-21

This release makes it possible to reset the clock with the "back" action when the clock is paused.

### Improvement

- Make the "back" action reset a paused clock

### Notes

- Force portrait orientation
- Move most logic out of LaunchedEffect

## [1.0.3](https://github.com/ldeso/blitz/releases/tag/v1.0.3) – 2024-02-20

This release enables resource shrinking and app optimization to bring the size of the APK below one megabyte.

### Improvement

- Enable resource shrinking and app optimization

## [1.0.2](https://github.com/ldeso/blitz/releases/tag/v1.0.2) – 2024-02-20

This release improves the app icon.

### Improvements

- Implement adaptive icon
- Remove circular icon
- Reduce vector path from 1909 to 674 characters

## [1.0.1](https://github.com/ldeso/blitz/releases/tag/v1.0.1) – 2024-02-20

This release fixes a bug where the time could still be incremented after reaching zero.

### Bug Fixes

- Do not increment when time is up
- Handle negative

## [1.0.0](https://github.com/ldeso/blitz/releases/tag/v1.0.0) – 2024-02-20

Initial release.
