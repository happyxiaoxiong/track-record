export class Utils {
  static formatMeters(size: number): String {
    const units = ['m', 'km'];
    return Utils.format(units, size, 1000, 2);
  }

  static formatBytes(len: number): String {
    const units = ['bytes', 'kb', 'mb', 'gb'];
    return Utils.format(units, len, 1024, 2);
  }

  private static format(units: Array<String>, num: number, unit: number, fixed: number): String {
    let i = 0;
    for (; i < units.length; ++i) {
      if (num < unit) {
        break;
      }
      num /= 1024;
    }
    return (num <= 0 ? '0' : num.toFixed(fixed).replace(/0+$/, '')) + units[i === units.length ? i - 1 : i];
  }

  static more(val: string, len?: number) {
    len = len || 8;
    if (val && val.length > len) {
      val = `<span title="${val}" data-toggle="tooltip">${val.substr(0, 8)}...</span>`;
    }
    return val;
  }

  static addHhMmSs(val: string) {
    return val && val.match(/^\d{4}(-\d\d){2}$/) ? val + ' 00:00:00' : '';
  }
}
