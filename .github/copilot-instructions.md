# Copilot Instructions (jptp)

This repository is a multi-module Maven project implementing the Picture Transfer Protocol (PTP) in Java.

## High-level goals
- Prefer **correctness, clarity, and small diffs** over clever solutions.
- Keep changes **backwards-compatible** unless explicitly requested.
- Avoid introducing new third-party dependencies unless necessary and justified.

## Project structure
- Root project: `pom` packaging, manages versions and plugins via `dependencyManagement`.
- Modules:
  - `jptp-core`: core protocol logic (should be platform-agnostic).
  - `jptp-usb`: PTP over USB (uses `usb4java`, includes JNI/native boundaries).
  - `jptp-main`: executable / application entry point.

## Language and tooling
- Java: **21** (use modern Java features where they improve readability).
- Build: Maven (multi-module).
- Testing: JUnit + Mockito + AssertJ with annotations.
- Coverage reporting: JaCoCo.
- Formatting: keep style consistent with existing code in the module you touch.

## Dependency and versioning rules
- Respect versions defined in the **root** `pom.xml` and `dependencyManagement`.
- In module `pom.xml` files, avoid specifying versions that are already managed.
- Be cautious with `usb4java`:
  - JNI/native methods are hard/impossible to mock; prefer abstraction and seam injection.
  - Do not change native interaction code without adding or adjusting tests around the Java-level contract.

## Coding guidelines
- Prefer **immutable** data and side-effect-free methods in `jptp-core`.
- Keep module boundaries clean:
  - `jptp-core` must not depend on USB-specific classes.
  - `jptp-usb` may depend on `jptp-core`, not vice-versa.
- Error handling:
  - Use meaningful exception types and messages.
  - Avoid swallowing exceptions; propagate or wrap with context.
- Logging:
  - Use `slf4j` (`LoggerFactory`), do not use `System.out`.
  - Keep logs structured and at appropriate levels.

## Tests
- Add or update tests when changing behavior.
- Prefer:
  - Unit tests for `jptp-core` logic.
  - Adapter/contract-style tests for `jptp-usb` Java-facing behavior (mock boundaries where possible).
- Mockito:
  - Mock interfaces/abstractions rather than concrete/native-driven classes.
  - Avoid brittle interaction-heavy tests unless necessary.

## Build and verification
- Ensure `mvn -q test` passes for relevant modules.
- Keep JaCoCo configuration intact.
- Do not modify Sonar configuration unless explicitly requested.

## Release/publishing constraints
- The project uses:
  - `flatten-maven-plugin` (OSS\-style flattened POMs).
  - GPG signing in the `release` profile.
  - Sonatype Central publishing plugin in the `release` profile.
- Do not change release plugin configuration unless explicitly requested.
- Do not add interactive steps; CI-friendly, non-interactive defaults are preferred.

## Documentation
- Maintain or add Javadoc for public APIs (especially in `jptp-core`).
- Keep module descriptions and artifact naming consistent.

## When implementing changes
- First, identify the module impacted and keep changes contained.
- Prefer minimal APIs and clear naming over broad refactors.
- If refactoring, do it in small steps and keep behavior identical unless the task requires changes.