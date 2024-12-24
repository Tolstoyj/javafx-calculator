# Known Issues

## Current Version (1.0.0)

### UI/UX Issues

#### 🐛 Window Movement Restriction on macOS
- **Issue**: After installing the application on macOS, the calculator window cannot be moved from its initial position.
- **Impact**: Users cannot reposition the calculator window on their screen.
- **Workaround**: None currently available.
- **Status**: Under investigation
- **Planned Fix**: Version 1.0.1
- **GitHub Issue**: [#1](https://github.com/Tolstoyj/javafx-calculator/issues/1)

### Installation Issues

#### ⚠️ First Launch Security Warning on macOS
- **Issue**: macOS may show a security warning on first launch since the app is not notarized.
- **Workaround**: Right-click the app and select "Open" for the first launch.

### Performance Issues

#### 🔍 Memory Usage
- **Issue**: Memory usage may increase during extended calculator sessions.
- **Workaround**: Restart the application if you notice performance degradation.

## Reporting New Issues

If you encounter an issue not listed here:
1. Check if it's already reported in our [GitHub Issues](https://github.com/Tolstoyj/javafx-calculator/issues)
2. If not, please create a new issue using our bug report template
3. Include as much detail as possible, including:
   - Your operating system and version
   - Java version
   - Steps to reproduce
   - Screenshots if applicable

## Issue Status Definitions

- 🔴 **Critical**: Severely impacts core functionality
- 🟡 **Major**: Affects important features but has workarounds
- 🟢 **Minor**: Cosmetic or non-essential functionality issues
- ✅ **Fixed**: Issue has been resolved
- 📝 **Under Investigation**: Issue is being analyzed
- 🚧 **In Progress**: Fix is being developed

## Fixed in Previous Versions

### Version 1.0.0
- Initial release

## Planned Fixes

### Version 1.0.1 (Planned)
- Window movement functionality on macOS
- Memory optimization improvements
- Additional error handling for edge cases

### Version 1.1.0 (Planned)
- Enhanced UI responsiveness
- Improved platform-specific behaviors
- Additional customization options 