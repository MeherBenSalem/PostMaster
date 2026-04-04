# PostMaster — Changelog

## [1.0.5] — 2026-04-04

### Fixed
- **Jar size reduced from ~17.5 MB → 7.1 MB** via targeted shade filters:
  - `sqlite-jdbc` native binaries for Windows, macOS, FreeBSD, Android, 32-bit Linux (x86/armv7), and ppc64 are no longer bundled — only Linux x86_64 and aarch64 (both glibc and musl) are kept, covering all production server environments including Alpine/Docker.
  - MySQL X DevAPI / X Protocol classes (`com.mysql.cj.x`, `com.mysql.cj.xdevapi`) excluded — these are unused for standard JDBC connections.
- **HikariCP relocated** to `io.nightbeam.postmaster.libs.hikari` to prevent classpath conflicts when other plugins also shade HikariCP.
- **JDBC driver SPI** entries (`META-INF/services/java.sql.Driver`) are now correctly merged via `ServicesResourceTransformer` so both the SQLite and MySQL drivers register at startup.

---

## [1.0.4] — 2026-04-03

### Improved
- Voucher command placeholder replacement is now **case-insensitive** (e.g. `%Player%` works as well as `%player%`).
- Added `%player_name%` and `%name%` as accepted aliases for `%player%` in voucher command definitions.

---

## [1.0.3]

### Fixed
- Permission node corrections.

---

## [1.0.1]

### Fixed
- Minor bug fixes.
