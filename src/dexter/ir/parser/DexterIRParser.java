// Generated from DexterIR.g4 by ANTLR 4.7.2

package dexter.ir.parser;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DexterIRParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38, 
		COMMENT=39, LINE_COMMENT=40, WS=41, EQ=42, NEQ=43, AND=44, OR=45, NOT=46, 
		LT=47, LE=48, GT=49, GE=50, PLUS=51, MINUS=52, MULT=53, DIV=54, MOD=55, 
		SHL=56, LSHR=57, ASHR=58, BAND=59, BOR=60, BXOR=61, BNOT=62, BASETYPE=63, 
		MACRO=64, ID=65, NUMBER=66, FLOATNUM=67;
	public static final int
		RULE_program = 0, RULE_uFnDecl = 1, RULE_fnDecl = 2, RULE_classDecl = 3, 
		RULE_varDecl = 4, RULE_type = 5, RULE_forIter = 6, RULE_expr = 7, RULE_forr = 8, 
		RULE_inr = 9;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "uFnDecl", "fnDecl", "classDecl", "varDecl", "type", "forIter", 
			"expr", "forr", "inr"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "','", "')'", "':'", "'->'", "'class'", "'array'", "'tuple'", 
			"'list'", "'ptr'", "'bv'", "'for'", "'in'", "'true'", "'false'", "'empty'", 
			"'uninterp'", "'null'", "'concrete'", "'const'", "'['", "']'", "'incr'", 
			"'decr'", "'.'", "'@'", "'emit'", "'init'", "'scope'", "'lb'", "'ub'", 
			"'forall'", "'if'", "'then'", "'else'", "'-->'", "'let'", "'assume'", 
			null, null, null, "'='", "'!='", "'&&'", "'||'", "'!'", "'<'", "'<='", 
			"'>'", "'>='", "'+'", "'-'", "'*'", "'/'", "'%'", "'<<'", "'>>'", "'>>>'", 
			"'&'", "'|'", "'^'", "'~'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, "COMMENT", "LINE_COMMENT", "WS", "EQ", "NEQ", "AND", 
			"OR", "NOT", "LT", "LE", "GT", "GE", "PLUS", "MINUS", "MULT", "DIV", 
			"MOD", "SHL", "LSHR", "ASHR", "BAND", "BOR", "BXOR", "BNOT", "BASETYPE", 
			"MACRO", "ID", "NUMBER", "FLOATNUM"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "DexterIR.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public DexterIRParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgramContext extends ParserRuleContext {
		public List<ClassDeclContext> classDecl() {
			return getRuleContexts(ClassDeclContext.class);
		}
		public ClassDeclContext classDecl(int i) {
			return getRuleContext(ClassDeclContext.class,i);
		}
		public List<FnDeclContext> fnDecl() {
			return getRuleContexts(FnDeclContext.class);
		}
		public FnDeclContext fnDecl(int i) {
			return getRuleContext(FnDeclContext.class,i);
		}
		public List<UFnDeclContext> uFnDecl() {
			return getRuleContexts(UFnDeclContext.class);
		}
		public UFnDeclContext uFnDecl(int i) {
			return getRuleContext(UFnDeclContext.class,i);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitProgram(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(25);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(23);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
					case 1:
						{
						setState(20);
						classDecl();
						}
						break;
					case 2:
						{
						setState(21);
						fnDecl();
						}
						break;
					case 3:
						{
						setState(22);
						uFnDecl();
						}
						break;
					}
					} 
				}
				setState(27);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			}
			setState(29);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__9) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__22) | (1L << T__23) | (1L << T__25) | (1L << T__31) | (1L << T__32) | (1L << T__36) | (1L << NOT) | (1L << PLUS) | (1L << MINUS) | (1L << BNOT))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (ID - 65)) | (1L << (NUMBER - 65)) | (1L << (FLOATNUM - 65)))) != 0)) {
				{
				setState(28);
				expr(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UFnDeclContext extends ParserRuleContext {
		public Token name;
		public TypeContext retType;
		public List<VarDeclContext> varDecl() {
			return getRuleContexts(VarDeclContext.class);
		}
		public VarDeclContext varDecl(int i) {
			return getRuleContext(VarDeclContext.class,i);
		}
		public TerminalNode ID() { return getToken(DexterIRParser.ID, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public UFnDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_uFnDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterUFnDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitUFnDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitUFnDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UFnDeclContext uFnDecl() throws RecognitionException {
		UFnDeclContext _localctx = new UFnDeclContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_uFnDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(31);
			((UFnDeclContext)_localctx).name = match(ID);
			setState(32);
			match(T__0);
			setState(33);
			varDecl();
			setState(38);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(34);
				match(T__1);
				setState(35);
				varDecl();
				}
				}
				setState(40);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(41);
			match(T__2);
			setState(42);
			match(T__3);
			setState(43);
			((UFnDeclContext)_localctx).retType = type();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FnDeclContext extends ParserRuleContext {
		public Token name;
		public TypeContext retType;
		public ExprContext body;
		public List<VarDeclContext> varDecl() {
			return getRuleContexts(VarDeclContext.class);
		}
		public VarDeclContext varDecl(int i) {
			return getRuleContext(VarDeclContext.class,i);
		}
		public TerminalNode ID() { return getToken(DexterIRParser.ID, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public FnDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fnDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterFnDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitFnDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitFnDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FnDeclContext fnDecl() throws RecognitionException {
		FnDeclContext _localctx = new FnDeclContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_fnDecl);
		int _la;
		try {
			setState(69);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(45);
				((FnDeclContext)_localctx).name = match(ID);
				setState(46);
				match(T__0);
				setState(47);
				varDecl();
				setState(52);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__1) {
					{
					{
					setState(48);
					match(T__1);
					setState(49);
					varDecl();
					}
					}
					setState(54);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(55);
				match(T__2);
				setState(56);
				match(T__3);
				setState(57);
				((FnDeclContext)_localctx).retType = type();
				setState(58);
				match(T__4);
				setState(59);
				((FnDeclContext)_localctx).body = expr(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(61);
				((FnDeclContext)_localctx).name = match(ID);
				setState(62);
				match(T__0);
				setState(63);
				match(T__2);
				setState(64);
				match(T__3);
				setState(65);
				((FnDeclContext)_localctx).retType = type();
				setState(66);
				match(T__4);
				setState(67);
				((FnDeclContext)_localctx).body = expr(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassDeclContext extends ParserRuleContext {
		public Token name;
		public List<VarDeclContext> varDecl() {
			return getRuleContexts(VarDeclContext.class);
		}
		public VarDeclContext varDecl(int i) {
			return getRuleContext(VarDeclContext.class,i);
		}
		public TerminalNode ID() { return getToken(DexterIRParser.ID, 0); }
		public ClassDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterClassDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitClassDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitClassDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassDeclContext classDecl() throws RecognitionException {
		ClassDeclContext _localctx = new ClassDeclContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_classDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(71);
			match(T__5);
			setState(72);
			((ClassDeclContext)_localctx).name = match(ID);
			setState(73);
			match(T__0);
			setState(74);
			varDecl();
			setState(79);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(75);
				match(T__1);
				setState(76);
				varDecl();
				}
				}
				setState(81);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(82);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarDeclContext extends ParserRuleContext {
		public Token name;
		public TypeContext t;
		public TerminalNode ID() { return getToken(DexterIRParser.ID, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public VarDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterVarDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitVarDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitVarDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDeclContext varDecl() throws RecognitionException {
		VarDeclContext _localctx = new VarDeclContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_varDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(84);
			((VarDeclContext)_localctx).name = match(ID);
			setState(85);
			match(T__3);
			setState(86);
			((VarDeclContext)_localctx).t = type();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeContext extends ParserRuleContext {
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
	 
		public TypeContext() { }
		public void copyFrom(TypeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ArrayTypeContext extends TypeContext {
		public Token dim;
		public TypeContext elemT;
		public TerminalNode NUMBER() { return getToken(DexterIRParser.NUMBER, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ArrayTypeContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterArrayType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitArrayType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitArrayType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PointerTypeContext extends TypeContext {
		public TypeContext elemT;
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public PointerTypeContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterPointerType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitPointerType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitPointerType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BitvectorTypeContext extends TypeContext {
		public Token w;
		public TerminalNode NUMBER() { return getToken(DexterIRParser.NUMBER, 0); }
		public BitvectorTypeContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterBitvectorType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitBitvectorType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitBitvectorType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FnTypeContext extends TypeContext {
		public TypeContext retT;
		public List<TypeContext> type() {
			return getRuleContexts(TypeContext.class);
		}
		public TypeContext type(int i) {
			return getRuleContext(TypeContext.class,i);
		}
		public FnTypeContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterFnType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitFnType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitFnType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TupleTypeContext extends TypeContext {
		public Token sz;
		public TypeContext elemT;
		public TerminalNode NUMBER() { return getToken(DexterIRParser.NUMBER, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TupleTypeContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterTupleType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitTupleType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitTupleType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ListTypeContext extends TypeContext {
		public TypeContext elemT;
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ListTypeContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterListType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitListType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitListType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ClassTypeContext extends TypeContext {
		public Token name;
		public TerminalNode ID() { return getToken(DexterIRParser.ID, 0); }
		public ClassTypeContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterClassType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitClassType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitClassType(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BaseTypeContext extends TypeContext {
		public TerminalNode BASETYPE() { return getToken(DexterIRParser.BASETYPE, 0); }
		public BaseTypeContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterBaseType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitBaseType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitBaseType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_type);
		int _la;
		try {
			setState(132);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				_localctx = new ClassTypeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(88);
				((ClassTypeContext)_localctx).name = match(ID);
				}
				break;
			case T__6:
				_localctx = new ArrayTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(89);
				match(T__6);
				setState(90);
				match(T__0);
				setState(91);
				((ArrayTypeContext)_localctx).dim = match(NUMBER);
				setState(92);
				match(T__1);
				setState(93);
				((ArrayTypeContext)_localctx).elemT = type();
				setState(94);
				match(T__2);
				}
				break;
			case T__7:
				_localctx = new TupleTypeContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(96);
				match(T__7);
				setState(97);
				match(T__0);
				setState(98);
				((TupleTypeContext)_localctx).sz = match(NUMBER);
				setState(99);
				match(T__1);
				setState(100);
				((TupleTypeContext)_localctx).elemT = type();
				setState(101);
				match(T__2);
				}
				break;
			case T__8:
				_localctx = new ListTypeContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(103);
				match(T__8);
				setState(104);
				match(T__0);
				setState(105);
				((ListTypeContext)_localctx).elemT = type();
				setState(106);
				match(T__2);
				}
				break;
			case T__9:
				_localctx = new PointerTypeContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(108);
				match(T__9);
				setState(109);
				match(T__0);
				setState(110);
				((PointerTypeContext)_localctx).elemT = type();
				setState(111);
				match(T__2);
				}
				break;
			case T__0:
				_localctx = new FnTypeContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(113);
				match(T__0);
				setState(122);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << BASETYPE))) != 0) || _la==ID) {
					{
					setState(114);
					type();
					setState(119);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__1) {
						{
						{
						setState(115);
						match(T__1);
						setState(116);
						type();
						}
						}
						setState(121);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(124);
				match(T__2);
				setState(125);
				match(T__4);
				setState(126);
				((FnTypeContext)_localctx).retT = type();
				}
				break;
			case T__10:
				_localctx = new BitvectorTypeContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(127);
				match(T__10);
				setState(128);
				match(T__0);
				setState(129);
				((BitvectorTypeContext)_localctx).w = match(NUMBER);
				setState(130);
				match(T__2);
				}
				break;
			case BASETYPE:
				_localctx = new BaseTypeContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(131);
				match(BASETYPE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ForIterContext extends ParserRuleContext {
		public Token v;
		public ExprContext l;
		public TerminalNode ID() { return getToken(DexterIRParser.ID, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ForIterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forIter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterForIter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitForIter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitForIter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForIterContext forIter() throws RecognitionException {
		ForIterContext _localctx = new ForIterContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_forIter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(134);
			match(T__0);
			setState(135);
			match(T__11);
			setState(136);
			((ForIterContext)_localctx).v = match(ID);
			setState(137);
			match(T__12);
			setState(138);
			((ForIterContext)_localctx).l = expr(0);
			setState(139);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class InitMacroContext extends ExprContext {
		public ExprContext v;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public InitMacroContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterInitMacro(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitInitMacro(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitInitMacro(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NullExprContext extends ExprContext {
		public TypeContext t;
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public NullExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterNullExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitNullExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitNullExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VarsMacroContext extends ExprContext {
		public Token m;
		public TypeContext t;
		public TerminalNode MACRO() { return getToken(DexterIRParser.MACRO, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public VarsMacroContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterVarsMacro(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitVarsMacro(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitVarsMacro(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ScopeMacroContext extends ExprContext {
		public ExprContext v;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ScopeMacroContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterScopeMacro(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitScopeMacro(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitScopeMacro(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ParenExprContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ParenExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterParenExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitParenExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitParenExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LetExprContext extends ExprContext {
		public ExprContext body;
		public ExprContext as;
		public List<VarDeclContext> varDecl() {
			return getRuleContexts(VarDeclContext.class);
		}
		public VarDeclContext varDecl(int i) {
			return getRuleContext(VarDeclContext.class,i);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public LetExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterLetExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitLetExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitLetExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PtrExprContext extends ExprContext {
		public ExprContext d;
		public ExprContext o;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public PtrExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterPtrExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitPtrExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitPtrExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnaryExprContext extends ExprContext {
		public Token op;
		public ExprContext b;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode PLUS() { return getToken(DexterIRParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(DexterIRParser.MINUS, 0); }
		public TerminalNode NOT() { return getToken(DexterIRParser.NOT, 0); }
		public TerminalNode BNOT() { return getToken(DexterIRParser.BNOT, 0); }
		public UnaryExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterUnaryExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitUnaryExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitUnaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BoundsMacroContext extends ExprContext {
		public Token m;
		public Token idx;
		public TerminalNode NUMBER() { return getToken(DexterIRParser.NUMBER, 0); }
		public BoundsMacroContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterBoundsMacro(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitBoundsMacro(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitBoundsMacro(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LambdaExprContext extends ExprContext {
		public TypeContext retType;
		public ExprContext body;
		public List<VarDeclContext> varDecl() {
			return getRuleContexts(VarDeclContext.class);
		}
		public VarDeclContext varDecl(int i) {
			return getRuleContext(VarDeclContext.class,i);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public LambdaExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterLambdaExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitLambdaExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitLambdaExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ForEachMacroContext extends ExprContext {
		public Token v;
		public ExprContext m;
		public ExprContext body;
		public TerminalNode ID() { return getToken(DexterIRParser.ID, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ForEachMacroContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterForEachMacro(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitForEachMacro(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitForEachMacro(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IfExprContext extends ExprContext {
		public ExprContext cond;
		public ExprContext cons;
		public ExprContext alt;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public IfExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterIfExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitIfExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitIfExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CIntLitExprContext extends ExprContext {
		public Token v;
		public TerminalNode NUMBER() { return getToken(DexterIRParser.NUMBER, 0); }
		public CIntLitExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterCIntLitExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitCIntLitExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitCIntLitExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CallExprContext extends ExprContext {
		public Token name;
		public TerminalNode ID() { return getToken(DexterIRParser.ID, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public CallExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterCallExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitCallExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitCallExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ForallExprContext extends ExprContext {
		public Token v;
		public ExprContext start;
		public ExprContext end;
		public ExprContext body;
		public TerminalNode ID() { return getToken(DexterIRParser.ID, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ForallExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterForallExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitForallExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitForallExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IntLitExprContext extends ExprContext {
		public TerminalNode NUMBER() { return getToken(DexterIRParser.NUMBER, 0); }
		public IntLitExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterIntLitExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitIntLitExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitIntLitExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BinaryExprContext extends ExprContext {
		public ExprContext l;
		public Token op;
		public ExprContext r;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode MULT() { return getToken(DexterIRParser.MULT, 0); }
		public TerminalNode DIV() { return getToken(DexterIRParser.DIV, 0); }
		public TerminalNode MOD() { return getToken(DexterIRParser.MOD, 0); }
		public TerminalNode PLUS() { return getToken(DexterIRParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(DexterIRParser.MINUS, 0); }
		public TerminalNode SHL() { return getToken(DexterIRParser.SHL, 0); }
		public TerminalNode LSHR() { return getToken(DexterIRParser.LSHR, 0); }
		public TerminalNode ASHR() { return getToken(DexterIRParser.ASHR, 0); }
		public TerminalNode BAND() { return getToken(DexterIRParser.BAND, 0); }
		public TerminalNode BOR() { return getToken(DexterIRParser.BOR, 0); }
		public TerminalNode BXOR() { return getToken(DexterIRParser.BXOR, 0); }
		public TerminalNode LE() { return getToken(DexterIRParser.LE, 0); }
		public TerminalNode GE() { return getToken(DexterIRParser.GE, 0); }
		public TerminalNode LT() { return getToken(DexterIRParser.LT, 0); }
		public TerminalNode GT() { return getToken(DexterIRParser.GT, 0); }
		public TerminalNode EQ() { return getToken(DexterIRParser.EQ, 0); }
		public TerminalNode NEQ() { return getToken(DexterIRParser.NEQ, 0); }
		public TerminalNode AND() { return getToken(DexterIRParser.AND, 0); }
		public TerminalNode OR() { return getToken(DexterIRParser.OR, 0); }
		public BinaryExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterBinaryExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitBinaryExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitBinaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IncrPtrExprContext extends ExprContext {
		public ExprContext p;
		public ExprContext o;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public IncrPtrExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterIncrPtrExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitIncrPtrExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitIncrPtrExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class EmptyListExprContext extends ExprContext {
		public TypeContext elemT;
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public EmptyListExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterEmptyListExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitEmptyListExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitEmptyListExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DecrPtrExprContext extends ExprContext {
		public ExprContext p;
		public ExprContext o;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public DecrPtrExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterDecrPtrExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitDecrPtrExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitDecrPtrExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FloatLitExprContext extends ExprContext {
		public TerminalNode FLOATNUM() { return getToken(DexterIRParser.FLOATNUM, 0); }
		public FloatLitExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterFloatLitExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitFloatLitExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitFloatLitExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UninterpExprContext extends ExprContext {
		public Token t;
		public TerminalNode BASETYPE() { return getToken(DexterIRParser.BASETYPE, 0); }
		public UninterpExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterUninterpExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitUninterpExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitUninterpExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FieldExprContext extends ExprContext {
		public ExprContext obj;
		public Token field;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode ID() { return getToken(DexterIRParser.ID, 0); }
		public FieldExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterFieldExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitFieldExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitFieldExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ImplyExprContext extends ExprContext {
		public ExprContext ls;
		public ExprContext rs;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ImplyExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterImplyExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitImplyExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitImplyExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ListCompExprContext extends ExprContext {
		public ExprContext e;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<ForIterContext> forIter() {
			return getRuleContexts(ForIterContext.class);
		}
		public ForIterContext forIter(int i) {
			return getRuleContext(ForIterContext.class,i);
		}
		public ListCompExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterListCompExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitListCompExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitListCompExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VarExprContext extends ExprContext {
		public TerminalNode ID() { return getToken(DexterIRParser.ID, 0); }
		public VarExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterVarExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitVarExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitVarExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BoolLitExprContext extends ExprContext {
		public BoolLitExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterBoolLitExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitBoolLitExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitBoolLitExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ChooseExprContext extends ExprContext {
		public Token t;
		public Token bw;
		public TerminalNode BASETYPE() { return getToken(DexterIRParser.BASETYPE, 0); }
		public TerminalNode NUMBER() { return getToken(DexterIRParser.NUMBER, 0); }
		public ChooseExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterChooseExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitChooseExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitChooseExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TupleExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TupleExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterTupleExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitTupleExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitTupleExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 14;
		enterRecursionRule(_localctx, 14, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(325);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				{
				_localctx = new ParenExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(142);
				match(T__0);
				setState(143);
				expr(0);
				setState(144);
				match(T__2);
				}
				break;
			case 2:
				{
				_localctx = new BoolLitExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(146);
				_la = _input.LA(1);
				if ( !(_la==T__13 || _la==T__14) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 3:
				{
				_localctx = new IntLitExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(147);
				match(NUMBER);
				}
				break;
			case 4:
				{
				_localctx = new FloatLitExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(148);
				match(FLOATNUM);
				}
				break;
			case 5:
				{
				_localctx = new EmptyListExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(149);
				match(T__15);
				setState(150);
				match(T__0);
				setState(151);
				((EmptyListExprContext)_localctx).elemT = type();
				setState(152);
				match(T__2);
				}
				break;
			case 6:
				{
				_localctx = new UninterpExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(154);
				match(T__16);
				setState(155);
				match(T__0);
				setState(156);
				((UninterpExprContext)_localctx).t = match(BASETYPE);
				setState(157);
				match(T__2);
				}
				break;
			case 7:
				{
				_localctx = new NullExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(158);
				match(T__17);
				setState(159);
				match(T__0);
				setState(160);
				((NullExprContext)_localctx).t = type();
				setState(161);
				match(T__2);
				}
				break;
			case 8:
				{
				_localctx = new CIntLitExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(163);
				match(T__18);
				setState(164);
				match(T__0);
				setState(165);
				((CIntLitExprContext)_localctx).v = match(NUMBER);
				setState(166);
				match(T__2);
				}
				break;
			case 9:
				{
				_localctx = new ChooseExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(167);
				match(T__19);
				setState(168);
				match(T__0);
				setState(169);
				((ChooseExprContext)_localctx).t = match(BASETYPE);
				setState(170);
				match(T__1);
				setState(171);
				((ChooseExprContext)_localctx).bw = match(NUMBER);
				setState(172);
				match(T__2);
				}
				break;
			case 10:
				{
				_localctx = new TupleExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(173);
				match(T__20);
				setState(174);
				expr(0);
				setState(179);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__1) {
					{
					{
					setState(175);
					match(T__1);
					setState(176);
					expr(0);
					}
					}
					setState(181);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(182);
				match(T__21);
				}
				break;
			case 11:
				{
				_localctx = new PtrExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(184);
				match(T__9);
				setState(185);
				match(T__0);
				setState(186);
				((PtrExprContext)_localctx).d = expr(0);
				setState(187);
				match(T__1);
				setState(188);
				((PtrExprContext)_localctx).o = expr(0);
				setState(189);
				match(T__2);
				}
				break;
			case 12:
				{
				_localctx = new IncrPtrExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(191);
				match(T__22);
				setState(192);
				match(T__0);
				setState(193);
				((IncrPtrExprContext)_localctx).p = expr(0);
				setState(194);
				match(T__1);
				setState(195);
				((IncrPtrExprContext)_localctx).o = expr(0);
				setState(196);
				match(T__2);
				}
				break;
			case 13:
				{
				_localctx = new DecrPtrExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(198);
				match(T__23);
				setState(199);
				match(T__0);
				setState(200);
				((DecrPtrExprContext)_localctx).p = expr(0);
				setState(201);
				match(T__1);
				setState(202);
				((DecrPtrExprContext)_localctx).o = expr(0);
				setState(203);
				match(T__2);
				}
				break;
			case 14:
				{
				_localctx = new ForEachMacroContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(205);
				match(T__25);
				setState(206);
				match(T__11);
				setState(207);
				match(T__0);
				setState(208);
				((ForEachMacroContext)_localctx).v = match(ID);
				setState(209);
				match(T__12);
				setState(210);
				((ForEachMacroContext)_localctx).m = expr(0);
				setState(211);
				match(T__2);
				setState(212);
				match(T__26);
				setState(213);
				((ForEachMacroContext)_localctx).body = expr(24);
				}
				break;
			case 15:
				{
				_localctx = new InitMacroContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(215);
				match(T__25);
				setState(216);
				match(T__27);
				setState(217);
				match(T__0);
				setState(218);
				((InitMacroContext)_localctx).v = expr(0);
				setState(219);
				match(T__2);
				}
				break;
			case 16:
				{
				_localctx = new ScopeMacroContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(221);
				match(T__25);
				setState(222);
				match(T__28);
				setState(223);
				match(T__0);
				setState(224);
				((ScopeMacroContext)_localctx).v = expr(0);
				setState(225);
				match(T__2);
				}
				break;
			case 17:
				{
				_localctx = new VarsMacroContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(227);
				match(T__25);
				setState(228);
				((VarsMacroContext)_localctx).m = match(MACRO);
				setState(229);
				match(T__0);
				setState(230);
				((VarsMacroContext)_localctx).t = type();
				setState(231);
				match(T__2);
				}
				break;
			case 18:
				{
				_localctx = new BoundsMacroContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(233);
				match(T__25);
				setState(234);
				((BoundsMacroContext)_localctx).m = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__29 || _la==T__30) ) {
					((BoundsMacroContext)_localctx).m = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(235);
				match(T__0);
				setState(236);
				((BoundsMacroContext)_localctx).idx = match(NUMBER);
				setState(237);
				match(T__2);
				}
				break;
			case 19:
				{
				_localctx = new CallExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(238);
				((CallExprContext)_localctx).name = match(ID);
				setState(239);
				match(T__0);
				setState(240);
				match(T__2);
				}
				break;
			case 20:
				{
				_localctx = new CallExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(241);
				((CallExprContext)_localctx).name = match(ID);
				setState(242);
				match(T__0);
				setState(243);
				expr(0);
				setState(248);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__1) {
					{
					{
					setState(244);
					match(T__1);
					setState(245);
					expr(0);
					}
					}
					setState(250);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(251);
				match(T__2);
				}
				break;
			case 21:
				{
				_localctx = new VarExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(253);
				match(ID);
				}
				break;
			case 22:
				{
				_localctx = new UnaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(254);
				((UnaryExprContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==PLUS || _la==MINUS) ) {
					((UnaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(255);
				((UnaryExprContext)_localctx).b = expr(15);
				}
				break;
			case 23:
				{
				_localctx = new UnaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(256);
				((UnaryExprContext)_localctx).op = match(NOT);
				setState(257);
				((UnaryExprContext)_localctx).b = expr(14);
				}
				break;
			case 24:
				{
				_localctx = new UnaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(258);
				((UnaryExprContext)_localctx).op = match(BNOT);
				setState(259);
				((UnaryExprContext)_localctx).b = expr(13);
				}
				break;
			case 25:
				{
				_localctx = new ForallExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(260);
				match(T__31);
				setState(261);
				match(T__0);
				setState(262);
				((ForallExprContext)_localctx).v = match(ID);
				setState(263);
				match(T__1);
				setState(264);
				((ForallExprContext)_localctx).start = expr(0);
				setState(265);
				match(T__1);
				setState(266);
				((ForallExprContext)_localctx).end = expr(0);
				setState(267);
				match(T__1);
				setState(268);
				((ForallExprContext)_localctx).body = expr(0);
				setState(269);
				match(T__2);
				}
				break;
			case 26:
				{
				_localctx = new IfExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(271);
				match(T__32);
				setState(272);
				((IfExprContext)_localctx).cond = expr(0);
				setState(273);
				match(T__33);
				setState(274);
				((IfExprContext)_localctx).cons = expr(0);
				setState(275);
				match(T__34);
				setState(276);
				((IfExprContext)_localctx).alt = expr(5);
				}
				break;
			case 27:
				{
				_localctx = new LambdaExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(278);
				match(T__0);
				setState(279);
				match(T__0);
				setState(280);
				varDecl();
				setState(285);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__1) {
					{
					{
					setState(281);
					match(T__1);
					setState(282);
					varDecl();
					}
					}
					setState(287);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(288);
				match(T__2);
				setState(289);
				match(T__3);
				setState(290);
				((LambdaExprContext)_localctx).retType = type();
				setState(291);
				match(T__4);
				setState(292);
				((LambdaExprContext)_localctx).body = expr(0);
				setState(293);
				match(T__2);
				}
				break;
			case 28:
				{
				_localctx = new LetExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(295);
				match(T__36);
				setState(296);
				match(T__0);
				setState(297);
				varDecl();
				setState(302);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__1) {
					{
					{
					setState(298);
					match(T__1);
					setState(299);
					varDecl();
					}
					}
					setState(304);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(305);
				match(T__2);
				setState(306);
				match(T__12);
				setState(307);
				((LetExprContext)_localctx).body = expr(2);
				}
				break;
			case 29:
				{
				_localctx = new LetExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(309);
				match(T__36);
				setState(310);
				match(T__0);
				setState(311);
				varDecl();
				setState(316);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__1) {
					{
					{
					setState(312);
					match(T__1);
					setState(313);
					varDecl();
					}
					}
					setState(318);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(319);
				match(T__2);
				setState(320);
				match(T__12);
				setState(321);
				((LetExprContext)_localctx).body = expr(0);
				setState(322);
				match(T__37);
				setState(323);
				((LetExprContext)_localctx).as = expr(1);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(359);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(357);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
					case 1:
						{
						_localctx = new BinaryExprContext(new ExprContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).l = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(327);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(328);
						((BinaryExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MULT) | (1L << DIV) | (1L << MOD))) != 0)) ) {
							((BinaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(329);
						((BinaryExprContext)_localctx).r = expr(13);
						}
						break;
					case 2:
						{
						_localctx = new BinaryExprContext(new ExprContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).l = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(330);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(331);
						((BinaryExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==PLUS || _la==MINUS) ) {
							((BinaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(332);
						((BinaryExprContext)_localctx).r = expr(12);
						}
						break;
					case 3:
						{
						_localctx = new BinaryExprContext(new ExprContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).l = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(333);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(334);
						((BinaryExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SHL) | (1L << LSHR) | (1L << ASHR) | (1L << BAND) | (1L << BOR) | (1L << BXOR))) != 0)) ) {
							((BinaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(335);
						((BinaryExprContext)_localctx).r = expr(11);
						}
						break;
					case 4:
						{
						_localctx = new BinaryExprContext(new ExprContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).l = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(336);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(337);
						((BinaryExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LT) | (1L << LE) | (1L << GT) | (1L << GE))) != 0)) ) {
							((BinaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(338);
						((BinaryExprContext)_localctx).r = expr(10);
						}
						break;
					case 5:
						{
						_localctx = new BinaryExprContext(new ExprContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).l = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(339);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(340);
						((BinaryExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==EQ || _la==NEQ) ) {
							((BinaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(341);
						((BinaryExprContext)_localctx).r = expr(9);
						}
						break;
					case 6:
						{
						_localctx = new BinaryExprContext(new ExprContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).l = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(342);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(343);
						((BinaryExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==AND || _la==OR) ) {
							((BinaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(344);
						((BinaryExprContext)_localctx).r = expr(8);
						}
						break;
					case 7:
						{
						_localctx = new ImplyExprContext(new ExprContext(_parentctx, _parentState));
						((ImplyExprContext)_localctx).ls = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(345);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(346);
						match(T__35);
						setState(347);
						((ImplyExprContext)_localctx).rs = expr(5);
						}
						break;
					case 8:
						{
						_localctx = new FieldExprContext(new ExprContext(_parentctx, _parentState));
						((FieldExprContext)_localctx).obj = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(348);
						if (!(precpred(_ctx, 25))) throw new FailedPredicateException(this, "precpred(_ctx, 25)");
						setState(349);
						match(T__24);
						setState(350);
						((FieldExprContext)_localctx).field = match(ID);
						}
						break;
					case 9:
						{
						_localctx = new ListCompExprContext(new ExprContext(_parentctx, _parentState));
						((ListCompExprContext)_localctx).e = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(351);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(353); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(352);
								forIter();
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(355); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						break;
					}
					} 
				}
				setState(361);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ForrContext extends ParserRuleContext {
		public ForrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterForr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitForr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitForr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForrContext forr() throws RecognitionException {
		ForrContext _localctx = new ForrContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_forr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(362);
			match(T__11);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InrContext extends ParserRuleContext {
		public InrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterInr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitInr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitInr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InrContext inr() throws RecognitionException {
		InrContext _localctx = new InrContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_inr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(364);
			match(T__12);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 7:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 12);
		case 1:
			return precpred(_ctx, 11);
		case 2:
			return precpred(_ctx, 10);
		case 3:
			return precpred(_ctx, 9);
		case 4:
			return precpred(_ctx, 8);
		case 5:
			return precpred(_ctx, 7);
		case 6:
			return precpred(_ctx, 4);
		case 7:
			return precpred(_ctx, 25);
		case 8:
			return precpred(_ctx, 16);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3E\u0171\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\3\2\3\2\3\2\7\2\32\n\2\f\2\16\2\35\13\2\3\2\5\2 \n\2\3\3\3\3\3\3\3"+
		"\3\3\3\7\3\'\n\3\f\3\16\3*\13\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\7"+
		"\4\65\n\4\f\4\16\48\13\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\5\4H\n\4\3\5\3\5\3\5\3\5\3\5\3\5\7\5P\n\5\f\5\16\5S\13\5\3"+
		"\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\7"+
		"\7x\n\7\f\7\16\7{\13\7\5\7}\n\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\5\7\u0087"+
		"\n\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3"+
		"\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\7\t\u00b4\n\t\f\t\16\t\u00b7\13\t"+
		"\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3"+
		"\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3"+
		"\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\7\t\u00f9\n\t\f\t\16\t"+
		"\u00fc\13\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3"+
		"\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t"+
		"\7\t\u011e\n\t\f\t\16\t\u0121\13\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\t\7\t\u012f\n\t\f\t\16\t\u0132\13\t\3\t\3\t\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\t\7\t\u013d\n\t\f\t\16\t\u0140\13\t\3\t\3\t\3\t\3\t\3\t\3\t"+
		"\5\t\u0148\n\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\6\t\u0164\n\t\r\t\16"+
		"\t\u0165\7\t\u0168\n\t\f\t\16\t\u016b\13\t\3\n\3\n\3\13\3\13\3\13\2\3"+
		"\20\f\2\4\6\b\n\f\16\20\22\24\2\n\3\2\20\21\3\2 !\3\2\65\66\3\2\679\3"+
		"\2:?\3\2\61\64\3\2,-\3\2./\2\u01a2\2\33\3\2\2\2\4!\3\2\2\2\6G\3\2\2\2"+
		"\bI\3\2\2\2\nV\3\2\2\2\f\u0086\3\2\2\2\16\u0088\3\2\2\2\20\u0147\3\2\2"+
		"\2\22\u016c\3\2\2\2\24\u016e\3\2\2\2\26\32\5\b\5\2\27\32\5\6\4\2\30\32"+
		"\5\4\3\2\31\26\3\2\2\2\31\27\3\2\2\2\31\30\3\2\2\2\32\35\3\2\2\2\33\31"+
		"\3\2\2\2\33\34\3\2\2\2\34\37\3\2\2\2\35\33\3\2\2\2\36 \5\20\t\2\37\36"+
		"\3\2\2\2\37 \3\2\2\2 \3\3\2\2\2!\"\7C\2\2\"#\7\3\2\2#(\5\n\6\2$%\7\4\2"+
		"\2%\'\5\n\6\2&$\3\2\2\2\'*\3\2\2\2(&\3\2\2\2()\3\2\2\2)+\3\2\2\2*(\3\2"+
		"\2\2+,\7\5\2\2,-\7\6\2\2-.\5\f\7\2.\5\3\2\2\2/\60\7C\2\2\60\61\7\3\2\2"+
		"\61\66\5\n\6\2\62\63\7\4\2\2\63\65\5\n\6\2\64\62\3\2\2\2\658\3\2\2\2\66"+
		"\64\3\2\2\2\66\67\3\2\2\2\679\3\2\2\28\66\3\2\2\29:\7\5\2\2:;\7\6\2\2"+
		";<\5\f\7\2<=\7\7\2\2=>\5\20\t\2>H\3\2\2\2?@\7C\2\2@A\7\3\2\2AB\7\5\2\2"+
		"BC\7\6\2\2CD\5\f\7\2DE\7\7\2\2EF\5\20\t\2FH\3\2\2\2G/\3\2\2\2G?\3\2\2"+
		"\2H\7\3\2\2\2IJ\7\b\2\2JK\7C\2\2KL\7\3\2\2LQ\5\n\6\2MN\7\4\2\2NP\5\n\6"+
		"\2OM\3\2\2\2PS\3\2\2\2QO\3\2\2\2QR\3\2\2\2RT\3\2\2\2SQ\3\2\2\2TU\7\5\2"+
		"\2U\t\3\2\2\2VW\7C\2\2WX\7\6\2\2XY\5\f\7\2Y\13\3\2\2\2Z\u0087\7C\2\2["+
		"\\\7\t\2\2\\]\7\3\2\2]^\7D\2\2^_\7\4\2\2_`\5\f\7\2`a\7\5\2\2a\u0087\3"+
		"\2\2\2bc\7\n\2\2cd\7\3\2\2de\7D\2\2ef\7\4\2\2fg\5\f\7\2gh\7\5\2\2h\u0087"+
		"\3\2\2\2ij\7\13\2\2jk\7\3\2\2kl\5\f\7\2lm\7\5\2\2m\u0087\3\2\2\2no\7\f"+
		"\2\2op\7\3\2\2pq\5\f\7\2qr\7\5\2\2r\u0087\3\2\2\2s|\7\3\2\2ty\5\f\7\2"+
		"uv\7\4\2\2vx\5\f\7\2wu\3\2\2\2x{\3\2\2\2yw\3\2\2\2yz\3\2\2\2z}\3\2\2\2"+
		"{y\3\2\2\2|t\3\2\2\2|}\3\2\2\2}~\3\2\2\2~\177\7\5\2\2\177\u0080\7\7\2"+
		"\2\u0080\u0087\5\f\7\2\u0081\u0082\7\r\2\2\u0082\u0083\7\3\2\2\u0083\u0084"+
		"\7D\2\2\u0084\u0087\7\5\2\2\u0085\u0087\7A\2\2\u0086Z\3\2\2\2\u0086[\3"+
		"\2\2\2\u0086b\3\2\2\2\u0086i\3\2\2\2\u0086n\3\2\2\2\u0086s\3\2\2\2\u0086"+
		"\u0081\3\2\2\2\u0086\u0085\3\2\2\2\u0087\r\3\2\2\2\u0088\u0089\7\3\2\2"+
		"\u0089\u008a\7\16\2\2\u008a\u008b\7C\2\2\u008b\u008c\7\17\2\2\u008c\u008d"+
		"\5\20\t\2\u008d\u008e\7\5\2\2\u008e\17\3\2\2\2\u008f\u0090\b\t\1\2\u0090"+
		"\u0091\7\3\2\2\u0091\u0092\5\20\t\2\u0092\u0093\7\5\2\2\u0093\u0148\3"+
		"\2\2\2\u0094\u0148\t\2\2\2\u0095\u0148\7D\2\2\u0096\u0148\7E\2\2\u0097"+
		"\u0098\7\22\2\2\u0098\u0099\7\3\2\2\u0099\u009a\5\f\7\2\u009a\u009b\7"+
		"\5\2\2\u009b\u0148\3\2\2\2\u009c\u009d\7\23\2\2\u009d\u009e\7\3\2\2\u009e"+
		"\u009f\7A\2\2\u009f\u0148\7\5\2\2\u00a0\u00a1\7\24\2\2\u00a1\u00a2\7\3"+
		"\2\2\u00a2\u00a3\5\f\7\2\u00a3\u00a4\7\5\2\2\u00a4\u0148\3\2\2\2\u00a5"+
		"\u00a6\7\25\2\2\u00a6\u00a7\7\3\2\2\u00a7\u00a8\7D\2\2\u00a8\u0148\7\5"+
		"\2\2\u00a9\u00aa\7\26\2\2\u00aa\u00ab\7\3\2\2\u00ab\u00ac\7A\2\2\u00ac"+
		"\u00ad\7\4\2\2\u00ad\u00ae\7D\2\2\u00ae\u0148\7\5\2\2\u00af\u00b0\7\27"+
		"\2\2\u00b0\u00b5\5\20\t\2\u00b1\u00b2\7\4\2\2\u00b2\u00b4\5\20\t\2\u00b3"+
		"\u00b1\3\2\2\2\u00b4\u00b7\3\2\2\2\u00b5\u00b3\3\2\2\2\u00b5\u00b6\3\2"+
		"\2\2\u00b6\u00b8\3\2\2\2\u00b7\u00b5\3\2\2\2\u00b8\u00b9\7\30\2\2\u00b9"+
		"\u0148\3\2\2\2\u00ba\u00bb\7\f\2\2\u00bb\u00bc\7\3\2\2\u00bc\u00bd\5\20"+
		"\t\2\u00bd\u00be\7\4\2\2\u00be\u00bf\5\20\t\2\u00bf\u00c0\7\5\2\2\u00c0"+
		"\u0148\3\2\2\2\u00c1\u00c2\7\31\2\2\u00c2\u00c3\7\3\2\2\u00c3\u00c4\5"+
		"\20\t\2\u00c4\u00c5\7\4\2\2\u00c5\u00c6\5\20\t\2\u00c6\u00c7\7\5\2\2\u00c7"+
		"\u0148\3\2\2\2\u00c8\u00c9\7\32\2\2\u00c9\u00ca\7\3\2\2\u00ca\u00cb\5"+
		"\20\t\2\u00cb\u00cc\7\4\2\2\u00cc\u00cd\5\20\t\2\u00cd\u00ce\7\5\2\2\u00ce"+
		"\u0148\3\2\2\2\u00cf\u00d0\7\34\2\2\u00d0\u00d1\7\16\2\2\u00d1\u00d2\7"+
		"\3\2\2\u00d2\u00d3\7C\2\2\u00d3\u00d4\7\17\2\2\u00d4\u00d5\5\20\t\2\u00d5"+
		"\u00d6\7\5\2\2\u00d6\u00d7\7\35\2\2\u00d7\u00d8\5\20\t\32\u00d8\u0148"+
		"\3\2\2\2\u00d9\u00da\7\34\2\2\u00da\u00db\7\36\2\2\u00db\u00dc\7\3\2\2"+
		"\u00dc\u00dd\5\20\t\2\u00dd\u00de\7\5\2\2\u00de\u0148\3\2\2\2\u00df\u00e0"+
		"\7\34\2\2\u00e0\u00e1\7\37\2\2\u00e1\u00e2\7\3\2\2\u00e2\u00e3\5\20\t"+
		"\2\u00e3\u00e4\7\5\2\2\u00e4\u0148\3\2\2\2\u00e5\u00e6\7\34\2\2\u00e6"+
		"\u00e7\7B\2\2\u00e7\u00e8\7\3\2\2\u00e8\u00e9\5\f\7\2\u00e9\u00ea\7\5"+
		"\2\2\u00ea\u0148\3\2\2\2\u00eb\u00ec\7\34\2\2\u00ec\u00ed\t\3\2\2\u00ed"+
		"\u00ee\7\3\2\2\u00ee\u00ef\7D\2\2\u00ef\u0148\7\5\2\2\u00f0\u00f1\7C\2"+
		"\2\u00f1\u00f2\7\3\2\2\u00f2\u0148\7\5\2\2\u00f3\u00f4\7C\2\2\u00f4\u00f5"+
		"\7\3\2\2\u00f5\u00fa\5\20\t\2\u00f6\u00f7\7\4\2\2\u00f7\u00f9\5\20\t\2"+
		"\u00f8\u00f6\3\2\2\2\u00f9\u00fc\3\2\2\2\u00fa\u00f8\3\2\2\2\u00fa\u00fb"+
		"\3\2\2\2\u00fb\u00fd\3\2\2\2\u00fc\u00fa\3\2\2\2\u00fd\u00fe\7\5\2\2\u00fe"+
		"\u0148\3\2\2\2\u00ff\u0148\7C\2\2\u0100\u0101\t\4\2\2\u0101\u0148\5\20"+
		"\t\21\u0102\u0103\7\60\2\2\u0103\u0148\5\20\t\20\u0104\u0105\7@\2\2\u0105"+
		"\u0148\5\20\t\17\u0106\u0107\7\"\2\2\u0107\u0108\7\3\2\2\u0108\u0109\7"+
		"C\2\2\u0109\u010a\7\4\2\2\u010a\u010b\5\20\t\2\u010b\u010c\7\4\2\2\u010c"+
		"\u010d\5\20\t\2\u010d\u010e\7\4\2\2\u010e\u010f\5\20\t\2\u010f\u0110\7"+
		"\5\2\2\u0110\u0148\3\2\2\2\u0111\u0112\7#\2\2\u0112\u0113\5\20\t\2\u0113"+
		"\u0114\7$\2\2\u0114\u0115\5\20\t\2\u0115\u0116\7%\2\2\u0116\u0117\5\20"+
		"\t\7\u0117\u0148\3\2\2\2\u0118\u0119\7\3\2\2\u0119\u011a\7\3\2\2\u011a"+
		"\u011f\5\n\6\2\u011b\u011c\7\4\2\2\u011c\u011e\5\n\6\2\u011d\u011b\3\2"+
		"\2\2\u011e\u0121\3\2\2\2\u011f\u011d\3\2\2\2\u011f\u0120\3\2\2\2\u0120"+
		"\u0122\3\2\2\2\u0121\u011f\3\2\2\2\u0122\u0123\7\5\2\2\u0123\u0124\7\6"+
		"\2\2\u0124\u0125\5\f\7\2\u0125\u0126\7\7\2\2\u0126\u0127\5\20\t\2\u0127"+
		"\u0128\7\5\2\2\u0128\u0148\3\2\2\2\u0129\u012a\7\'\2\2\u012a\u012b\7\3"+
		"\2\2\u012b\u0130\5\n\6\2\u012c\u012d\7\4\2\2\u012d\u012f\5\n\6\2\u012e"+
		"\u012c\3\2\2\2\u012f\u0132\3\2\2\2\u0130\u012e\3\2\2\2\u0130\u0131\3\2"+
		"\2\2\u0131\u0133\3\2\2\2\u0132\u0130\3\2\2\2\u0133\u0134\7\5\2\2\u0134"+
		"\u0135\7\17\2\2\u0135\u0136\5\20\t\4\u0136\u0148\3\2\2\2\u0137\u0138\7"+
		"\'\2\2\u0138\u0139\7\3\2\2\u0139\u013e\5\n\6\2\u013a\u013b\7\4\2\2\u013b"+
		"\u013d\5\n\6\2\u013c\u013a\3\2\2\2\u013d\u0140\3\2\2\2\u013e\u013c\3\2"+
		"\2\2\u013e\u013f\3\2\2\2\u013f\u0141\3\2\2\2\u0140\u013e\3\2\2\2\u0141"+
		"\u0142\7\5\2\2\u0142\u0143\7\17\2\2\u0143\u0144\5\20\t\2\u0144\u0145\7"+
		"(\2\2\u0145\u0146\5\20\t\3\u0146\u0148\3\2\2\2\u0147\u008f\3\2\2\2\u0147"+
		"\u0094\3\2\2\2\u0147\u0095\3\2\2\2\u0147\u0096\3\2\2\2\u0147\u0097\3\2"+
		"\2\2\u0147\u009c\3\2\2\2\u0147\u00a0\3\2\2\2\u0147\u00a5\3\2\2\2\u0147"+
		"\u00a9\3\2\2\2\u0147\u00af\3\2\2\2\u0147\u00ba\3\2\2\2\u0147\u00c1\3\2"+
		"\2\2\u0147\u00c8\3\2\2\2\u0147\u00cf\3\2\2\2\u0147\u00d9\3\2\2\2\u0147"+
		"\u00df\3\2\2\2\u0147\u00e5\3\2\2\2\u0147\u00eb\3\2\2\2\u0147\u00f0\3\2"+
		"\2\2\u0147\u00f3\3\2\2\2\u0147\u00ff\3\2\2\2\u0147\u0100\3\2\2\2\u0147"+
		"\u0102\3\2\2\2\u0147\u0104\3\2\2\2\u0147\u0106\3\2\2\2\u0147\u0111\3\2"+
		"\2\2\u0147\u0118\3\2\2\2\u0147\u0129\3\2\2\2\u0147\u0137\3\2\2\2\u0148"+
		"\u0169\3\2\2\2\u0149\u014a\f\16\2\2\u014a\u014b\t\5\2\2\u014b\u0168\5"+
		"\20\t\17\u014c\u014d\f\r\2\2\u014d\u014e\t\4\2\2\u014e\u0168\5\20\t\16"+
		"\u014f\u0150\f\f\2\2\u0150\u0151\t\6\2\2\u0151\u0168\5\20\t\r\u0152\u0153"+
		"\f\13\2\2\u0153\u0154\t\7\2\2\u0154\u0168\5\20\t\f\u0155\u0156\f\n\2\2"+
		"\u0156\u0157\t\b\2\2\u0157\u0168\5\20\t\13\u0158\u0159\f\t\2\2\u0159\u015a"+
		"\t\t\2\2\u015a\u0168\5\20\t\n\u015b\u015c\f\6\2\2\u015c\u015d\7&\2\2\u015d"+
		"\u0168\5\20\t\7\u015e\u015f\f\33\2\2\u015f\u0160\7\33\2\2\u0160\u0168"+
		"\7C\2\2\u0161\u0163\f\22\2\2\u0162\u0164\5\16\b\2\u0163\u0162\3\2\2\2"+
		"\u0164\u0165\3\2\2\2\u0165\u0163\3\2\2\2\u0165\u0166\3\2\2\2\u0166\u0168"+
		"\3\2\2\2\u0167\u0149\3\2\2\2\u0167\u014c\3\2\2\2\u0167\u014f\3\2\2\2\u0167"+
		"\u0152\3\2\2\2\u0167\u0155\3\2\2\2\u0167\u0158\3\2\2\2\u0167\u015b\3\2"+
		"\2\2\u0167\u015e\3\2\2\2\u0167\u0161\3\2\2\2\u0168\u016b\3\2\2\2\u0169"+
		"\u0167\3\2\2\2\u0169\u016a\3\2\2\2\u016a\21\3\2\2\2\u016b\u0169\3\2\2"+
		"\2\u016c\u016d\7\16\2\2\u016d\23\3\2\2\2\u016e\u016f\7\17\2\2\u016f\25"+
		"\3\2\2\2\25\31\33\37(\66GQy|\u0086\u00b5\u00fa\u011f\u0130\u013e\u0147"+
		"\u0165\u0167\u0169";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}