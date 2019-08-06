package dexter.ir;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class Util
{
  public static <T,U> List<U> map (List<T> l, Function<T, U> f)
  {
    return l.stream().map(f).collect(Collectors.toList());
  }
}