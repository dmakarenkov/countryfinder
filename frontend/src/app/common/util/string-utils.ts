export class StringUtils {
  public static isEmpty(object: string): boolean {
    if (!object) {
      return true;
    }
    return object.trim().length === 0;
  }
}
