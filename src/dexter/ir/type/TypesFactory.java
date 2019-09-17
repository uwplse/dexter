package dexter.ir.type;

import dexter.Preferences;
import dexter.ir.bool.ClassDecl;
import dexter.ir.bool.FuncDecl;
import dexter.ir.bool.Program;
import dexter.ir.bool.VarExpr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class TypesFactory
{
  public static final Type Int = new IntT();
  public static final Type Int8 = new Int8T();
  public static final Type Int16 = new Int16T();
  public static final Type Int32 = new Int32T();
  public static final Type UInt8 = new UInt8T();
  public static final Type UInt16 = new UInt16T();
  public static final Type UInt32 = new UInt32T();
  public static final Type Bool = new BoolT();
  public static final Type Float = new FloatT();

  protected static Map<Type, PtrT> ptrT = new HashMap<>();
  protected static Map<Map.Entry<Integer, Type>, ArrayT> arrayT = new HashMap<>();
  protected static Map<Map.Entry<Type, Integer>, BufferT> bufferT = new HashMap<>();
  protected static Map<Map.Entry<String, Map.Entry<Type, List<Type>>>, FunctionT> functionsT = new HashMap<>();
  protected static Map<String, ClassT> classT = new HashMap<>();
  protected static Map<Type, ListT> listT = new HashMap<>();
  protected static Map<Map.Entry<Integer, Type>, TupleT> tupleT = new HashMap<>();
  protected static Map<Integer, BitvectorT> bitvectorT = new HashMap<>();

  /*
    Since this class is static, this is called on each new parse
   */
  public static void reset ()
  {
    arrayT = new HashMap<>();
    functionsT = new HashMap<>();
    classT = new HashMap<>();
    listT = new HashMap<>();
    bitvectorT = new HashMap<>();
  }

  public static void loadDSL (String dslFilePath) throws IOException {
    if (dslFilePath.equals(""))
      return;

    FileInputStream fis = new FileInputStream(new File(dslFilePath));
    Program p = null;
    try {
      p = (Program) dexter.ir.parser.Util.parse(fis, true);
    } catch (Exception e) {
      System.out.println("Something went wrong while parsing DSL: " + e.getMessage());
      System.exit(1);
    }
    fis.close();

    if (Preferences.Global.verbosity > 0) {
      System.out.println("Loading DSL...");
      System.out.println(p.classes().size() + " class definitions found");
      System.out.println(p.functions().size() + " function definitions found");
    }

    for (ClassDecl c : p.classes()){
      classT(c.name(), c.fields());
    }

    for (FuncDecl fn : p.functions()){
      List<Type> paramsT = new ArrayList<>();

      for (VarExpr param : fn.params())
        paramsT.add(param.type());

      functionT(fn.name(), fn.retType(), paramsT);
    }
  }

  public static boolean isPrimitiveT(Type t) {
    return
        t == Int || t == Float || t == Bool || t instanceof BitvectorT ||
        t == Int8 || t == Int16 || t == Int32 ||
        t == UInt8 || t == UInt16 || t == UInt32;
  }

  public static FunctionT functionT(String name, Type returnT, List<Type> params)
  {
    Map.Entry<Type, List<Type>> sig = new AbstractMap.SimpleImmutableEntry<>(returnT, params);
    Map.Entry<String, Map.Entry<Type, List<Type>>> key = new AbstractMap.SimpleImmutableEntry<>(name, sig);
    FunctionT r = functionsT.get(key);
    if (r == null)
    {
      r = new FunctionT(name, params, returnT);
      functionsT.put(key, r);
    }
    return r;
  }

  public static FunctionT lookupFunctionT(String name, Type returnT, List<Type> params)
  {
    Map.Entry<Type, List<Type>> sig = new AbstractMap.SimpleImmutableEntry<>(returnT, params);
    Map.Entry<String, Map.Entry<Type, List<Type>>> key = new AbstractMap.SimpleImmutableEntry<>(name, sig);
    FunctionT r = functionsT.get(key);
    if (r == null)
      throw new RuntimeException(name + " function not found");

    return r;
  }

  public static boolean isFunctionT(Type t)
  {
    return functionsT.containsValue(t);
  }

  public static boolean isFunctionT(String name, Type returnT, List<Type> params)
  {
    Map.Entry<Type, List<Type>> sig = new AbstractMap.SimpleImmutableEntry<>(returnT, params);
    Map.Entry<String, Map.Entry<Type, List<Type>>> key = new AbstractMap.SimpleImmutableEntry<>(name, sig);
    return functionsT.containsKey(key);
  }

  public static BitvectorT bitvectorT(int width)
  {
    BitvectorT r = bitvectorT.get(width);
    if (r == null)
    {
      r = new BitvectorT(width);
      bitvectorT.put(width, r);
    }
    return r;
  }

  public static boolean isBitvectorT(Type t)
  {
    return bitvectorT.containsValue(t);
  }

  public static boolean isPtrT(Type t)
  {
    return ptrT.containsValue(t);
  }

  public static Type ptrT(Type elemsT) {
    PtrT r = ptrT.get(elemsT);
    if (r == null)
    {
      r = new PtrT(elemsT);
      ptrT.put(elemsT, r);
    }

    return r;
  }

  public static Collection<PtrT> ptrTypes () { return ptrT.values(); }

  public static ArrayT arrayT(int dim, Type elemsT)
  {
    Map.Entry<Integer, Type> key = new AbstractMap.SimpleImmutableEntry<>(dim, elemsT);
    ArrayT r = arrayT.get(key);
    if (r == null)
    {
      r = new ArrayT(dim, elemsT);
      arrayT.put(key, r);
    }

    return r;
  }

  public static boolean isArrayT(Type t)
  {
    return arrayT.containsValue(t);
  }

  public static BufferT bufferT(Type elemsT, int dim)
  {
    Map.Entry<Type, Integer> key = new AbstractMap.SimpleImmutableEntry<>(elemsT, dim);
    BufferT r = bufferT.get(key);
    if (r == null)
    {
      r = new BufferT(elemsT, dim);
      bufferT.put(key, r);
    }

    return r;
  }

  public static boolean isBufferT(Type t)
  {
    return bufferT.containsValue(t);
  }

  public static Collection<BufferT> bufferTypes() {
    return bufferT.values();
  }

  public static ListT listT(Type elemsT)
  {
    ListT r = listT.get(elemsT);
    if (r == null)
    {
      r = new ListT(elemsT);
      listT.put(elemsT, r);
    }

    return r;
  }

  public static boolean isListT(Type t)
  {
    return listT.containsValue(t);
  }

  public static Collection<ListT> listTypes () { return listT.values(); }

  public static TupleT tupleT(Type elemsT, int sz) {
    Map.Entry<Integer, Type> key = new AbstractMap.SimpleImmutableEntry<>(sz, elemsT);
    TupleT r = tupleT.get(key);
    if (r == null)
    {
      r = new TupleT(elemsT, sz);
      tupleT.put(key, r);
    }

    return r;
  }

  public static ClassT classT(String name, List<VarExpr> fields)
  {
    ClassT r = classT.get(name);
    if (r == null)
    {
      r = new ClassT(name, fields);
      classT.put(name, r);
    }
    else if (!r.fields().equals(fields))
      throw new RuntimeException("duplicate class declaration: " + name);

    return r;
  }

  public static ClassT lookupClassT(String name)
  {
    ClassT r = classT.get(name);
    if (r == null)
      throw new RuntimeException(name + " class not found");

    return r;
  }

  public static boolean isClassT(Type t) { return classT.containsValue(t); }

  public static boolean isClassT(String name) { return classT.get(name) != null; }
}