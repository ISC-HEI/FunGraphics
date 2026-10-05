# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

FunGraphics is a small Scala 2.13 / Java2D drawing library used to teach first-year programming at ISC (HES-SO Valais). It is distributed to students as a single jar they drop into IntelliJ projects, so **public signatures in `hevs.graphics` and `hevs.utils` are a de-facto frozen API**: lab material across several years depends on them. Add, don't rename or remove.

## Build and test

Gradle wrapper 7.4.2, Java toolchain 17 (the Scala plugin, not sbt). Source indentation is tabs.

```bash
./gradlew build          # compile, run tests, scaladoc, jar (jar depends on test and scaladoc)
./gradlew test           # JUnit 5 tests only
./gradlew test --tests 'hevs.graphics.utils.DisplaySetupTest'   # a single test class
./gradlew scaladoc       # output in build/docs/scaladoc (what the GitHub Pages site serves)
```

Outputs: `build/libs/FunGraphics-<gitversion>.jar` and a constant-name copy `build/FunGraphics-dev.jar` (the name comes from the directory name because `settings.gradle` sets no `rootProject.name`; the README's lowercase `fungraphics-dev.jar` is wrong on a case-sensitive FS).

The jar bundles the `.scala`/`.java` sources and the scaladoc HTML (so IntelliJ shows docs), plus `res/generated/version.txt` written by the `versionGen` task from `com.palantir.git-version`. `FunGraphics.version` reads that file; running from classes instead of the jar prints a "version will be wrong" warning, and an untagged commit prints a "non-release version" warning. Both are expected.

The jar does **not** contain the Scala standard library. To run the built-in demo you need it on the classpath, e.g. from the Gradle cache:

```bash
SL=$(find ~/.gradle/caches/modules-2 -name 'scala-library-2.13.9.jar' | head -1)
java -cp build/FunGraphics-dev.jar:$SL hevs.graphics.FunGraphics
```

Tests open real windows: `BasicTest` uses `assumeFalse(headless)` and is skipped without a display; the other tests are pure. Nothing in CI runs tests: the only workflow (`.github/workflows/scaladoc.yml`) publishes scaladoc to GitHub Pages when a tag is pushed. Releases are manual (tag `MAJOR.MINOR.SUB`, build, upload the jar to a GitHub release, see README).

## Architecture

Two independent window types live in `hevs.graphics`:

- **`AcceleratedDisplay` → `FunGraphics` → `TurtleGraphics` / `advanced.ListGraphics`**. `AcceleratedDisplay` owns the `JFrame` and two translucent `BufferedImage`s (`backBuffer`, `frontBuffer`) sized to the drawing area. A `SwingWorker` render thread copies back then front onto the frame's `BufferStrategy` at the screen refresh rate (`GraphicTimer.sync`). `FunGraphics` implements the `interfaces.Graphics` drawing API and `DualLayerGraphics`: `g2d` points at the front or the back buffer depending on `drawForeground()`/`drawBackground()`, and every primitive just draws on `g2d`. Student game loops draw on the main thread and call `syncGameLogic(fps)`; they synchronize on `frontBuffer` when flicker matters. `ListGraphics` keeps a list of `Drawable`s and redraws them in `repaint()`.
- **`ImageGraphics`** is a separate `JFrame` subclass for the image-processing lab (pixel arrays), unrelated to the above.

`hevs.utils` holds console helpers (`Input`, `TextTools`, `StringFunctions`, `DateUtils`) with no graphics dependency. `ScalaDocSetup.java` exists only because IntelliJ needs one `.java` file to treat the jar as having sources.

### Display handling on Linux (`hevs.graphics.utils.DisplaySetup`)

`DisplaySetup.prepare()` must run before any AWT/Swing class is touched (it is the first statement of `AcceleratedDisplay.initFrame` and runs through a constructor-argument trick in `ImageGraphics`). On Wayland desktops where X11 is unusable (typically IntelliJ installed as a Flatpak, which only has `fallback-x11`), it selects the Wayland toolkit if the JDK ships one (JetBrains Runtime). That toolkit has no page flipping, so `AcceleratedDisplay` then paints through a `JPanel` content pane (`renderPanel`) instead of a `BufferStrategy`. `DisplaySetup.openingDisplay { ... }` wraps the first display access and prints a tailored diagnosis (Flatpak override command, missing Xwayland, SSH, X authorization) before rethrowing.

To reproduce display failures locally, use `DISPLAY=unix:99` (fails instantly); plain `DISPLAY=:99` hangs for a minute under WSL because of the TCP fallback. WSLg provides both XWayland (`DISPLAY=:0`) and a Wayland socket at `/mnt/wslg/runtime-dir/wayland-0` (set `XDG_RUNTIME_DIR=/mnt/wslg/runtime-dir` to test a Wayland toolkit).

## Things that look like bugs but are load-bearing

- `SwingUtilities.invokeLater _ -> { ... }` in `AcceleratedDisplay.initFrame` builds a tuple, so the block runs synchronously on the calling thread, not on the EDT. The window is deliberately created and shown off-EDT; the rest of `initFrame` relies on `mainFrame` being set right after.
- Mouse and key listeners are attached to `mainFrame` itself (not the content pane), so mouse coordinates include the frame insets. Student code has adapted to that; changing it would silently shift every mouse-driven lab.
- Buffers are created with `Transparency.TRANSLUCENT`; `FunGraphics` clears the front buffer to white in its constructor and the background buffer stays transparent so the front layer composites over it.
- Resources are loaded with absolute classpath paths (`/res/img/...`, `/res/generated/...`) relative to `src/main/resources`.
- Scaladoc emits ~40 "Could not find any member to link" warnings for `[[String]]`-style links; they are harmless and the build is still green.

## Known open issues

GitHub issues #6 (NPE in `internalRender` when a window is disposed, which also kills the render thread) and #7 (window not or partially painted on Fedora/GNOME with OpenJDK 25) both live in the `BufferStrategy` render path of `AcceleratedDisplay`. The Swing-painting path added for Wayland is a candidate replacement for that whole mechanism.
