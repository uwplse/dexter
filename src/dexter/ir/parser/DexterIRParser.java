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
		T__38=39, T__39=40, COMMENT=41, LINE_COMMENT=42, WS=43, EQ=44, NEQ=45, 
		AND=46, OR=47, NOT=48, LT=49, LE=50, GT=51, GE=52, PLUS=53, MINUS=54, 
		MULT=55, DIV=56, MOD=57, SHL=58, LSHR=59, ASHR=60, BAND=61, BOR=62, BXOR=63, 
		BNOT=64, BASETYPE=65, MACRO=66, ID=67, NUMBER=68, FLOATNUM=69;
	public static final int
		RULE_program = 0, RULE_uFnDecl = 1, RULE_fnDecl = 2, RULE_genDecl = 3, 
		RULE_classDecl = 4, RULE_varDecl = 5, RULE_type = 6, RULE_forIter = 7, 
		RULE_expr = 8, RULE_forr = 9, RULE_inr = 10;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "uFnDecl", "fnDecl", "genDecl", "classDecl", "varDecl", "type", 
			"forIter", "expr", "forr", "inr"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "','", "')'", "':'", "'->'", "'generator'", "'class'", "'array'", 
			"'ptr'", "'buffer'", "'tuple'", "'list'", "'bv'", "'for'", "'in'", "'true'", 
			"'false'", "'empty'", "'uninterp'", "'null'", "'concrete'", "'const'", 
			"'['", "']'", "'incr'", "'decr'", "'.'", "'@'", "'emit'", "'init'", "'scope'", 
			"'lb'", "'ub'", "'forall'", "'if'", "'then'", "'else'", "'-->'", "'let'", 
			"'assume'", null, null, null, "'='", "'!='", "'&&'", "'||'", "'!'", "'<'", 
			"'<='", "'>'", "'>='", "'+'", "'-'", "'*'", "'/'", "'%'", "'<<'", "'>>'", 
			"'>>>'", "'&'", "'|'", "'^'", "'~'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, "COMMENT", "LINE_COMMENT", "WS", "EQ", 
			"NEQ", "AND", "OR", "NOT", "LT", "LE", "GT", "GE", "PLUS", "MINUS", "MULT", 
			"DIV", "MOD", "SHL", "LSHR", "ASHR", "BAND", "BOR", "BXOR", "BNOT", "BASETYPE", 
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
		public List<GenDeclContext> genDecl() {
			return getRuleContexts(GenDeclContext.class);
		}
		public GenDeclContext genDecl(int i) {
			return getRuleContext(GenDeclContext.class,i);
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
			setState(28);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(26);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
					case 1:
						{
						setState(22);
						classDecl();
						}
						break;
					case 2:
						{
						setState(23);
						fnDecl();
						}
						break;
					case 3:
						{
						setState(24);
						uFnDecl();
						}
						break;
					case 4:
						{
						setState(25);
						genDecl();
						}
						break;
					}
					} 
				}
				setState(30);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			}
			setState(32);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__8) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__24) | (1L << T__25) | (1L << T__27) | (1L << T__33) | (1L << T__34) | (1L << T__38) | (1L << NOT) | (1L << PLUS) | (1L << MINUS))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (BNOT - 64)) | (1L << (ID - 64)) | (1L << (NUMBER - 64)) | (1L << (FLOATNUM - 64)))) != 0)) {
				{
				setState(31);
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
			setState(34);
			((UFnDeclContext)_localctx).name = match(ID);
			setState(35);
			match(T__0);
			setState(36);
			varDecl();
			setState(41);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(37);
				match(T__1);
				setState(38);
				varDecl();
				}
				}
				setState(43);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(44);
			match(T__2);
			setState(45);
			match(T__3);
			setState(46);
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
			setState(72);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(48);
				((FnDeclContext)_localctx).name = match(ID);
				setState(49);
				match(T__0);
				setState(50);
				varDecl();
				setState(55);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__1) {
					{
					{
					setState(51);
					match(T__1);
					setState(52);
					varDecl();
					}
					}
					setState(57);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(58);
				match(T__2);
				setState(59);
				match(T__3);
				setState(60);
				((FnDeclContext)_localctx).retType = type();
				setState(61);
				match(T__4);
				setState(62);
				((FnDeclContext)_localctx).body = expr(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(64);
				((FnDeclContext)_localctx).name = match(ID);
				setState(65);
				match(T__0);
				setState(66);
				match(T__2);
				setState(67);
				match(T__3);
				setState(68);
				((FnDeclContext)_localctx).retType = type();
				setState(69);
				match(T__4);
				setState(70);
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

	public static class GenDeclContext extends ParserRuleContext {
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
		public GenDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_genDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterGenDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitGenDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitGenDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GenDeclContext genDecl() throws RecognitionException {
		GenDeclContext _localctx = new GenDeclContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_genDecl);
		int _la;
		try {
			setState(100);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(74);
				match(T__5);
				setState(75);
				((GenDeclContext)_localctx).name = match(ID);
				setState(76);
				match(T__0);
				setState(77);
				varDecl();
				setState(82);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__1) {
					{
					{
					setState(78);
					match(T__1);
					setState(79);
					varDecl();
					}
					}
					setState(84);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(85);
				match(T__2);
				setState(86);
				match(T__3);
				setState(87);
				((GenDeclContext)_localctx).retType = type();
				setState(88);
				match(T__4);
				setState(89);
				((GenDeclContext)_localctx).body = expr(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(91);
				match(T__5);
				setState(92);
				((GenDeclContext)_localctx).name = match(ID);
				setState(93);
				match(T__0);
				setState(94);
				match(T__2);
				setState(95);
				match(T__3);
				setState(96);
				((GenDeclContext)_localctx).retType = type();
				setState(97);
				match(T__4);
				setState(98);
				((GenDeclContext)_localctx).body = expr(0);
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
		enterRule(_localctx, 8, RULE_classDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(102);
			match(T__6);
			setState(103);
			((ClassDeclContext)_localctx).name = match(ID);
			setState(104);
			match(T__0);
			setState(105);
			varDecl();
			setState(110);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(106);
				match(T__1);
				setState(107);
				varDecl();
				}
				}
				setState(112);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(113);
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
		enterRule(_localctx, 10, RULE_varDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(115);
			((VarDeclContext)_localctx).name = match(ID);
			setState(116);
			match(T__3);
			setState(117);
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
	public static class BufferTypeContext extends TypeContext {
		public TypeContext elemT;
		public Token dim;
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode NUMBER() { return getToken(DexterIRParser.NUMBER, 0); }
		public BufferTypeContext(TypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).enterBufferType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DexterIRListener ) ((DexterIRListener)listener).exitBufferType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DexterIRVisitor ) return ((DexterIRVisitor<? extends T>)visitor).visitBufferType(this);
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
		enterRule(_localctx, 12, RULE_type);
		int _la;
		try {
			setState(170);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				_localctx = new ClassTypeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(119);
				((ClassTypeContext)_localctx).name = match(ID);
				}
				break;
			case T__7:
				_localctx = new ArrayTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(120);
				match(T__7);
				setState(121);
				match(T__0);
				setState(122);
				((ArrayTypeContext)_localctx).dim = match(NUMBER);
				setState(123);
				match(T__1);
				setState(124);
				((ArrayTypeContext)_localctx).elemT = type();
				setState(125);
				match(T__2);
				}
				break;
			case T__8:
				_localctx = new PointerTypeContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(127);
				match(T__8);
				setState(128);
				match(T__0);
				setState(129);
				((PointerTypeContext)_localctx).elemT = type();
				setState(130);
				match(T__2);
				}
				break;
			case T__9:
				_localctx = new BufferTypeContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(132);
				match(T__9);
				setState(133);
				match(T__0);
				setState(134);
				((BufferTypeContext)_localctx).elemT = type();
				setState(135);
				match(T__1);
				setState(136);
				((BufferTypeContext)_localctx).dim = match(NUMBER);
				setState(137);
				match(T__2);
				}
				break;
			case T__10:
				_localctx = new TupleTypeContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(139);
				match(T__10);
				setState(140);
				match(T__0);
				setState(141);
				((TupleTypeContext)_localctx).sz = match(NUMBER);
				setState(142);
				match(T__1);
				setState(143);
				((TupleTypeContext)_localctx).elemT = type();
				setState(144);
				match(T__2);
				}
				break;
			case T__11:
				_localctx = new ListTypeContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(146);
				match(T__11);
				setState(147);
				match(T__0);
				setState(148);
				((ListTypeContext)_localctx).elemT = type();
				setState(149);
				match(T__2);
				}
				break;
			case T__0:
				_localctx = new FnTypeContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(151);
				match(T__0);
				setState(160);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12))) != 0) || _la==BASETYPE || _la==ID) {
					{
					setState(152);
					type();
					setState(157);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__1) {
						{
						{
						setState(153);
						match(T__1);
						setState(154);
						type();
						}
						}
						setState(159);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(162);
				match(T__2);
				setState(163);
				match(T__4);
				setState(164);
				((FnTypeContext)_localctx).retT = type();
				}
				break;
			case T__12:
				_localctx = new BitvectorTypeContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(165);
				match(T__12);
				setState(166);
				match(T__0);
				setState(167);
				((BitvectorTypeContext)_localctx).w = match(NUMBER);
				setState(168);
				match(T__2);
				}
				break;
			case BASETYPE:
				_localctx = new BaseTypeContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(169);
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
		enterRule(_localctx, 14, RULE_forIter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(172);
			match(T__0);
			setState(173);
			match(T__13);
			setState(174);
			((ForIterContext)_localctx).v = match(ID);
			setState(175);
			match(T__14);
			setState(176);
			((ForIterContext)_localctx).l = expr(0);
			setState(177);
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
		int _startState = 16;
		enterRecursionRule(_localctx, 16, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(363);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				{
				_localctx = new ParenExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(180);
				match(T__0);
				setState(181);
				expr(0);
				setState(182);
				match(T__2);
				}
				break;
			case 2:
				{
				_localctx = new BoolLitExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(184);
				_la = _input.LA(1);
				if ( !(_la==T__15 || _la==T__16) ) {
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
				setState(185);
				match(NUMBER);
				}
				break;
			case 4:
				{
				_localctx = new FloatLitExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(186);
				match(FLOATNUM);
				}
				break;
			case 5:
				{
				_localctx = new EmptyListExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(187);
				match(T__17);
				setState(188);
				match(T__0);
				setState(189);
				((EmptyListExprContext)_localctx).elemT = type();
				setState(190);
				match(T__2);
				}
				break;
			case 6:
				{
				_localctx = new UninterpExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(192);
				match(T__18);
				setState(193);
				match(T__0);
				setState(194);
				((UninterpExprContext)_localctx).t = match(BASETYPE);
				setState(195);
				match(T__2);
				}
				break;
			case 7:
				{
				_localctx = new NullExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(196);
				match(T__19);
				setState(197);
				match(T__0);
				setState(198);
				((NullExprContext)_localctx).t = type();
				setState(199);
				match(T__2);
				}
				break;
			case 8:
				{
				_localctx = new CIntLitExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(201);
				match(T__20);
				setState(202);
				match(T__0);
				setState(203);
				((CIntLitExprContext)_localctx).v = match(NUMBER);
				setState(204);
				match(T__2);
				}
				break;
			case 9:
				{
				_localctx = new ChooseExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(205);
				match(T__21);
				setState(206);
				match(T__0);
				setState(207);
				((ChooseExprContext)_localctx).t = match(BASETYPE);
				setState(208);
				match(T__1);
				setState(209);
				((ChooseExprContext)_localctx).bw = match(NUMBER);
				setState(210);
				match(T__2);
				}
				break;
			case 10:
				{
				_localctx = new TupleExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(211);
				match(T__22);
				setState(212);
				expr(0);
				setState(217);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__1) {
					{
					{
					setState(213);
					match(T__1);
					setState(214);
					expr(0);
					}
					}
					setState(219);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(220);
				match(T__23);
				}
				break;
			case 11:
				{
				_localctx = new PtrExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(222);
				match(T__8);
				setState(223);
				match(T__0);
				setState(224);
				((PtrExprContext)_localctx).d = expr(0);
				setState(225);
				match(T__1);
				setState(226);
				((PtrExprContext)_localctx).o = expr(0);
				setState(227);
				match(T__2);
				}
				break;
			case 12:
				{
				_localctx = new IncrPtrExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(229);
				match(T__24);
				setState(230);
				match(T__0);
				setState(231);
				((IncrPtrExprContext)_localctx).p = expr(0);
				setState(232);
				match(T__1);
				setState(233);
				((IncrPtrExprContext)_localctx).o = expr(0);
				setState(234);
				match(T__2);
				}
				break;
			case 13:
				{
				_localctx = new DecrPtrExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(236);
				match(T__25);
				setState(237);
				match(T__0);
				setState(238);
				((DecrPtrExprContext)_localctx).p = expr(0);
				setState(239);
				match(T__1);
				setState(240);
				((DecrPtrExprContext)_localctx).o = expr(0);
				setState(241);
				match(T__2);
				}
				break;
			case 14:
				{
				_localctx = new ForEachMacroContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(243);
				match(T__27);
				setState(244);
				match(T__13);
				setState(245);
				match(T__0);
				setState(246);
				((ForEachMacroContext)_localctx).v = match(ID);
				setState(247);
				match(T__14);
				setState(248);
				((ForEachMacroContext)_localctx).m = expr(0);
				setState(249);
				match(T__2);
				setState(250);
				match(T__28);
				setState(251);
				((ForEachMacroContext)_localctx).body = expr(24);
				}
				break;
			case 15:
				{
				_localctx = new InitMacroContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(253);
				match(T__27);
				setState(254);
				match(T__29);
				setState(255);
				match(T__0);
				setState(256);
				((InitMacroContext)_localctx).v = expr(0);
				setState(257);
				match(T__2);
				}
				break;
			case 16:
				{
				_localctx = new ScopeMacroContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(259);
				match(T__27);
				setState(260);
				match(T__30);
				setState(261);
				match(T__0);
				setState(262);
				((ScopeMacroContext)_localctx).v = expr(0);
				setState(263);
				match(T__2);
				}
				break;
			case 17:
				{
				_localctx = new VarsMacroContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(265);
				match(T__27);
				setState(266);
				((VarsMacroContext)_localctx).m = match(MACRO);
				setState(267);
				match(T__0);
				setState(268);
				((VarsMacroContext)_localctx).t = type();
				setState(269);
				match(T__2);
				}
				break;
			case 18:
				{
				_localctx = new BoundsMacroContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(271);
				match(T__27);
				setState(272);
				((BoundsMacroContext)_localctx).m = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__31 || _la==T__32) ) {
					((BoundsMacroContext)_localctx).m = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(273);
				match(T__0);
				setState(274);
				((BoundsMacroContext)_localctx).idx = match(NUMBER);
				setState(275);
				match(T__2);
				}
				break;
			case 19:
				{
				_localctx = new CallExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(276);
				((CallExprContext)_localctx).name = match(ID);
				setState(277);
				match(T__0);
				setState(278);
				match(T__2);
				}
				break;
			case 20:
				{
				_localctx = new CallExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(279);
				((CallExprContext)_localctx).name = match(ID);
				setState(280);
				match(T__0);
				setState(281);
				expr(0);
				setState(286);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__1) {
					{
					{
					setState(282);
					match(T__1);
					setState(283);
					expr(0);
					}
					}
					setState(288);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(289);
				match(T__2);
				}
				break;
			case 21:
				{
				_localctx = new VarExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(291);
				match(ID);
				}
				break;
			case 22:
				{
				_localctx = new UnaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(292);
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
				setState(293);
				((UnaryExprContext)_localctx).b = expr(15);
				}
				break;
			case 23:
				{
				_localctx = new UnaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(294);
				((UnaryExprContext)_localctx).op = match(NOT);
				setState(295);
				((UnaryExprContext)_localctx).b = expr(14);
				}
				break;
			case 24:
				{
				_localctx = new UnaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(296);
				((UnaryExprContext)_localctx).op = match(BNOT);
				setState(297);
				((UnaryExprContext)_localctx).b = expr(13);
				}
				break;
			case 25:
				{
				_localctx = new ForallExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(298);
				match(T__33);
				setState(299);
				match(T__0);
				setState(300);
				((ForallExprContext)_localctx).v = match(ID);
				setState(301);
				match(T__1);
				setState(302);
				((ForallExprContext)_localctx).start = expr(0);
				setState(303);
				match(T__1);
				setState(304);
				((ForallExprContext)_localctx).end = expr(0);
				setState(305);
				match(T__1);
				setState(306);
				((ForallExprContext)_localctx).body = expr(0);
				setState(307);
				match(T__2);
				}
				break;
			case 26:
				{
				_localctx = new IfExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(309);
				match(T__34);
				setState(310);
				((IfExprContext)_localctx).cond = expr(0);
				setState(311);
				match(T__35);
				setState(312);
				((IfExprContext)_localctx).cons = expr(0);
				setState(313);
				match(T__36);
				setState(314);
				((IfExprContext)_localctx).alt = expr(5);
				}
				break;
			case 27:
				{
				_localctx = new LambdaExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(316);
				match(T__0);
				setState(317);
				match(T__0);
				setState(318);
				varDecl();
				setState(323);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__1) {
					{
					{
					setState(319);
					match(T__1);
					setState(320);
					varDecl();
					}
					}
					setState(325);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(326);
				match(T__2);
				setState(327);
				match(T__3);
				setState(328);
				((LambdaExprContext)_localctx).retType = type();
				setState(329);
				match(T__4);
				setState(330);
				((LambdaExprContext)_localctx).body = expr(0);
				setState(331);
				match(T__2);
				}
				break;
			case 28:
				{
				_localctx = new LetExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(333);
				match(T__38);
				setState(334);
				match(T__0);
				setState(335);
				varDecl();
				setState(340);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__1) {
					{
					{
					setState(336);
					match(T__1);
					setState(337);
					varDecl();
					}
					}
					setState(342);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(343);
				match(T__2);
				setState(344);
				match(T__14);
				setState(345);
				((LetExprContext)_localctx).body = expr(2);
				}
				break;
			case 29:
				{
				_localctx = new LetExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(347);
				match(T__38);
				setState(348);
				match(T__0);
				setState(349);
				varDecl();
				setState(354);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__1) {
					{
					{
					setState(350);
					match(T__1);
					setState(351);
					varDecl();
					}
					}
					setState(356);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(357);
				match(T__2);
				setState(358);
				match(T__14);
				setState(359);
				((LetExprContext)_localctx).body = expr(0);
				setState(360);
				match(T__39);
				setState(361);
				((LetExprContext)_localctx).as = expr(1);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(397);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(395);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
					case 1:
						{
						_localctx = new BinaryExprContext(new ExprContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).l = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(365);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(366);
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
						setState(367);
						((BinaryExprContext)_localctx).r = expr(13);
						}
						break;
					case 2:
						{
						_localctx = new BinaryExprContext(new ExprContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).l = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(368);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(369);
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
						setState(370);
						((BinaryExprContext)_localctx).r = expr(12);
						}
						break;
					case 3:
						{
						_localctx = new BinaryExprContext(new ExprContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).l = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(371);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(372);
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
						setState(373);
						((BinaryExprContext)_localctx).r = expr(11);
						}
						break;
					case 4:
						{
						_localctx = new BinaryExprContext(new ExprContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).l = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(374);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(375);
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
						setState(376);
						((BinaryExprContext)_localctx).r = expr(10);
						}
						break;
					case 5:
						{
						_localctx = new BinaryExprContext(new ExprContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).l = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(377);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(378);
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
						setState(379);
						((BinaryExprContext)_localctx).r = expr(9);
						}
						break;
					case 6:
						{
						_localctx = new BinaryExprContext(new ExprContext(_parentctx, _parentState));
						((BinaryExprContext)_localctx).l = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(380);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(381);
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
						setState(382);
						((BinaryExprContext)_localctx).r = expr(8);
						}
						break;
					case 7:
						{
						_localctx = new ImplyExprContext(new ExprContext(_parentctx, _parentState));
						((ImplyExprContext)_localctx).ls = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(383);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(384);
						match(T__37);
						setState(385);
						((ImplyExprContext)_localctx).rs = expr(5);
						}
						break;
					case 8:
						{
						_localctx = new FieldExprContext(new ExprContext(_parentctx, _parentState));
						((FieldExprContext)_localctx).obj = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(386);
						if (!(precpred(_ctx, 25))) throw new FailedPredicateException(this, "precpred(_ctx, 25)");
						setState(387);
						match(T__26);
						setState(388);
						((FieldExprContext)_localctx).field = match(ID);
						}
						break;
					case 9:
						{
						_localctx = new ListCompExprContext(new ExprContext(_parentctx, _parentState));
						((ListCompExprContext)_localctx).e = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(389);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(391); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(390);
								forIter();
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(393); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						break;
					}
					} 
				}
				setState(399);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
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
		enterRule(_localctx, 18, RULE_forr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(400);
			match(T__13);
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
		enterRule(_localctx, 20, RULE_inr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(402);
			match(T__14);
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
		case 8:
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3G\u0197\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\3\2\3\2\3\2\3\2\7\2\35\n\2\f\2\16\2 \13\2\3\2\5\2#\n\2\3\3"+
		"\3\3\3\3\3\3\3\3\7\3*\n\3\f\3\16\3-\13\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3"+
		"\4\3\4\7\48\n\4\f\4\16\4;\13\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\5\4K\n\4\3\5\3\5\3\5\3\5\3\5\3\5\7\5S\n\5\f\5\16\5V\13"+
		"\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5g\n"+
		"\5\3\6\3\6\3\6\3\6\3\6\3\6\7\6o\n\6\f\6\16\6r\13\6\3\6\3\6\3\7\3\7\3\7"+
		"\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3"+
		"\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b"+
		"\3\b\3\b\7\b\u009e\n\b\f\b\16\b\u00a1\13\b\5\b\u00a3\n\b\3\b\3\b\3\b\3"+
		"\b\3\b\3\b\3\b\3\b\5\b\u00ad\n\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3"+
		"\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\7\n\u00da"+
		"\n\n\f\n\16\n\u00dd\13\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3"+
		"\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3"+
		"\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\7\n\u011f\n\n\f\n\16\n\u0122\13\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3"+
		"\n\3\n\3\n\3\n\3\n\3\n\7\n\u0144\n\n\f\n\16\n\u0147\13\n\3\n\3\n\3\n\3"+
		"\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\7\n\u0155\n\n\f\n\16\n\u0158\13\n\3"+
		"\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\7\n\u0163\n\n\f\n\16\n\u0166\13\n\3"+
		"\n\3\n\3\n\3\n\3\n\3\n\5\n\u016e\n\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3"+
		"\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\6\n\u018a\n\n\r\n\16\n\u018b\7\n\u018e\n\n\f\n\16\n\u0191\13\n\3\13\3"+
		"\13\3\f\3\f\3\f\2\3\22\r\2\4\6\b\n\f\16\20\22\24\26\2\n\3\2\22\23\3\2"+
		"\"#\3\2\678\3\29;\3\2<A\3\2\63\66\3\2./\3\2\60\61\2\u01cb\2\36\3\2\2\2"+
		"\4$\3\2\2\2\6J\3\2\2\2\bf\3\2\2\2\nh\3\2\2\2\fu\3\2\2\2\16\u00ac\3\2\2"+
		"\2\20\u00ae\3\2\2\2\22\u016d\3\2\2\2\24\u0192\3\2\2\2\26\u0194\3\2\2\2"+
		"\30\35\5\n\6\2\31\35\5\6\4\2\32\35\5\4\3\2\33\35\5\b\5\2\34\30\3\2\2\2"+
		"\34\31\3\2\2\2\34\32\3\2\2\2\34\33\3\2\2\2\35 \3\2\2\2\36\34\3\2\2\2\36"+
		"\37\3\2\2\2\37\"\3\2\2\2 \36\3\2\2\2!#\5\22\n\2\"!\3\2\2\2\"#\3\2\2\2"+
		"#\3\3\2\2\2$%\7E\2\2%&\7\3\2\2&+\5\f\7\2\'(\7\4\2\2(*\5\f\7\2)\'\3\2\2"+
		"\2*-\3\2\2\2+)\3\2\2\2+,\3\2\2\2,.\3\2\2\2-+\3\2\2\2./\7\5\2\2/\60\7\6"+
		"\2\2\60\61\5\16\b\2\61\5\3\2\2\2\62\63\7E\2\2\63\64\7\3\2\2\649\5\f\7"+
		"\2\65\66\7\4\2\2\668\5\f\7\2\67\65\3\2\2\28;\3\2\2\29\67\3\2\2\29:\3\2"+
		"\2\2:<\3\2\2\2;9\3\2\2\2<=\7\5\2\2=>\7\6\2\2>?\5\16\b\2?@\7\7\2\2@A\5"+
		"\22\n\2AK\3\2\2\2BC\7E\2\2CD\7\3\2\2DE\7\5\2\2EF\7\6\2\2FG\5\16\b\2GH"+
		"\7\7\2\2HI\5\22\n\2IK\3\2\2\2J\62\3\2\2\2JB\3\2\2\2K\7\3\2\2\2LM\7\b\2"+
		"\2MN\7E\2\2NO\7\3\2\2OT\5\f\7\2PQ\7\4\2\2QS\5\f\7\2RP\3\2\2\2SV\3\2\2"+
		"\2TR\3\2\2\2TU\3\2\2\2UW\3\2\2\2VT\3\2\2\2WX\7\5\2\2XY\7\6\2\2YZ\5\16"+
		"\b\2Z[\7\7\2\2[\\\5\22\n\2\\g\3\2\2\2]^\7\b\2\2^_\7E\2\2_`\7\3\2\2`a\7"+
		"\5\2\2ab\7\6\2\2bc\5\16\b\2cd\7\7\2\2de\5\22\n\2eg\3\2\2\2fL\3\2\2\2f"+
		"]\3\2\2\2g\t\3\2\2\2hi\7\t\2\2ij\7E\2\2jk\7\3\2\2kp\5\f\7\2lm\7\4\2\2"+
		"mo\5\f\7\2nl\3\2\2\2or\3\2\2\2pn\3\2\2\2pq\3\2\2\2qs\3\2\2\2rp\3\2\2\2"+
		"st\7\5\2\2t\13\3\2\2\2uv\7E\2\2vw\7\6\2\2wx\5\16\b\2x\r\3\2\2\2y\u00ad"+
		"\7E\2\2z{\7\n\2\2{|\7\3\2\2|}\7F\2\2}~\7\4\2\2~\177\5\16\b\2\177\u0080"+
		"\7\5\2\2\u0080\u00ad\3\2\2\2\u0081\u0082\7\13\2\2\u0082\u0083\7\3\2\2"+
		"\u0083\u0084\5\16\b\2\u0084\u0085\7\5\2\2\u0085\u00ad\3\2\2\2\u0086\u0087"+
		"\7\f\2\2\u0087\u0088\7\3\2\2\u0088\u0089\5\16\b\2\u0089\u008a\7\4\2\2"+
		"\u008a\u008b\7F\2\2\u008b\u008c\7\5\2\2\u008c\u00ad\3\2\2\2\u008d\u008e"+
		"\7\r\2\2\u008e\u008f\7\3\2\2\u008f\u0090\7F\2\2\u0090\u0091\7\4\2\2\u0091"+
		"\u0092\5\16\b\2\u0092\u0093\7\5\2\2\u0093\u00ad\3\2\2\2\u0094\u0095\7"+
		"\16\2\2\u0095\u0096\7\3\2\2\u0096\u0097\5\16\b\2\u0097\u0098\7\5\2\2\u0098"+
		"\u00ad\3\2\2\2\u0099\u00a2\7\3\2\2\u009a\u009f\5\16\b\2\u009b\u009c\7"+
		"\4\2\2\u009c\u009e\5\16\b\2\u009d\u009b\3\2\2\2\u009e\u00a1\3\2\2\2\u009f"+
		"\u009d\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0\u00a3\3\2\2\2\u00a1\u009f\3\2"+
		"\2\2\u00a2\u009a\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a4"+
		"\u00a5\7\5\2\2\u00a5\u00a6\7\7\2\2\u00a6\u00ad\5\16\b\2\u00a7\u00a8\7"+
		"\17\2\2\u00a8\u00a9\7\3\2\2\u00a9\u00aa\7F\2\2\u00aa\u00ad\7\5\2\2\u00ab"+
		"\u00ad\7C\2\2\u00acy\3\2\2\2\u00acz\3\2\2\2\u00ac\u0081\3\2\2\2\u00ac"+
		"\u0086\3\2\2\2\u00ac\u008d\3\2\2\2\u00ac\u0094\3\2\2\2\u00ac\u0099\3\2"+
		"\2\2\u00ac\u00a7\3\2\2\2\u00ac\u00ab\3\2\2\2\u00ad\17\3\2\2\2\u00ae\u00af"+
		"\7\3\2\2\u00af\u00b0\7\20\2\2\u00b0\u00b1\7E\2\2\u00b1\u00b2\7\21\2\2"+
		"\u00b2\u00b3\5\22\n\2\u00b3\u00b4\7\5\2\2\u00b4\21\3\2\2\2\u00b5\u00b6"+
		"\b\n\1\2\u00b6\u00b7\7\3\2\2\u00b7\u00b8\5\22\n\2\u00b8\u00b9\7\5\2\2"+
		"\u00b9\u016e\3\2\2\2\u00ba\u016e\t\2\2\2\u00bb\u016e\7F\2\2\u00bc\u016e"+
		"\7G\2\2\u00bd\u00be\7\24\2\2\u00be\u00bf\7\3\2\2\u00bf\u00c0\5\16\b\2"+
		"\u00c0\u00c1\7\5\2\2\u00c1\u016e\3\2\2\2\u00c2\u00c3\7\25\2\2\u00c3\u00c4"+
		"\7\3\2\2\u00c4\u00c5\7C\2\2\u00c5\u016e\7\5\2\2\u00c6\u00c7\7\26\2\2\u00c7"+
		"\u00c8\7\3\2\2\u00c8\u00c9\5\16\b\2\u00c9\u00ca\7\5\2\2\u00ca\u016e\3"+
		"\2\2\2\u00cb\u00cc\7\27\2\2\u00cc\u00cd\7\3\2\2\u00cd\u00ce\7F\2\2\u00ce"+
		"\u016e\7\5\2\2\u00cf\u00d0\7\30\2\2\u00d0\u00d1\7\3\2\2\u00d1\u00d2\7"+
		"C\2\2\u00d2\u00d3\7\4\2\2\u00d3\u00d4\7F\2\2\u00d4\u016e\7\5\2\2\u00d5"+
		"\u00d6\7\31\2\2\u00d6\u00db\5\22\n\2\u00d7\u00d8\7\4\2\2\u00d8\u00da\5"+
		"\22\n\2\u00d9\u00d7\3\2\2\2\u00da\u00dd\3\2\2\2\u00db\u00d9\3\2\2\2\u00db"+
		"\u00dc\3\2\2\2\u00dc\u00de\3\2\2\2\u00dd\u00db\3\2\2\2\u00de\u00df\7\32"+
		"\2\2\u00df\u016e\3\2\2\2\u00e0\u00e1\7\13\2\2\u00e1\u00e2\7\3\2\2\u00e2"+
		"\u00e3\5\22\n\2\u00e3\u00e4\7\4\2\2\u00e4\u00e5\5\22\n\2\u00e5\u00e6\7"+
		"\5\2\2\u00e6\u016e\3\2\2\2\u00e7\u00e8\7\33\2\2\u00e8\u00e9\7\3\2\2\u00e9"+
		"\u00ea\5\22\n\2\u00ea\u00eb\7\4\2\2\u00eb\u00ec\5\22\n\2\u00ec\u00ed\7"+
		"\5\2\2\u00ed\u016e\3\2\2\2\u00ee\u00ef\7\34\2\2\u00ef\u00f0\7\3\2\2\u00f0"+
		"\u00f1\5\22\n\2\u00f1\u00f2\7\4\2\2\u00f2\u00f3\5\22\n\2\u00f3\u00f4\7"+
		"\5\2\2\u00f4\u016e\3\2\2\2\u00f5\u00f6\7\36\2\2\u00f6\u00f7\7\20\2\2\u00f7"+
		"\u00f8\7\3\2\2\u00f8\u00f9\7E\2\2\u00f9\u00fa\7\21\2\2\u00fa\u00fb\5\22"+
		"\n\2\u00fb\u00fc\7\5\2\2\u00fc\u00fd\7\37\2\2\u00fd\u00fe\5\22\n\32\u00fe"+
		"\u016e\3\2\2\2\u00ff\u0100\7\36\2\2\u0100\u0101\7 \2\2\u0101\u0102\7\3"+
		"\2\2\u0102\u0103\5\22\n\2\u0103\u0104\7\5\2\2\u0104\u016e\3\2\2\2\u0105"+
		"\u0106\7\36\2\2\u0106\u0107\7!\2\2\u0107\u0108\7\3\2\2\u0108\u0109\5\22"+
		"\n\2\u0109\u010a\7\5\2\2\u010a\u016e\3\2\2\2\u010b\u010c\7\36\2\2\u010c"+
		"\u010d\7D\2\2\u010d\u010e\7\3\2\2\u010e\u010f\5\16\b\2\u010f\u0110\7\5"+
		"\2\2\u0110\u016e\3\2\2\2\u0111\u0112\7\36\2\2\u0112\u0113\t\3\2\2\u0113"+
		"\u0114\7\3\2\2\u0114\u0115\7F\2\2\u0115\u016e\7\5\2\2\u0116\u0117\7E\2"+
		"\2\u0117\u0118\7\3\2\2\u0118\u016e\7\5\2\2\u0119\u011a\7E\2\2\u011a\u011b"+
		"\7\3\2\2\u011b\u0120\5\22\n\2\u011c\u011d\7\4\2\2\u011d\u011f\5\22\n\2"+
		"\u011e\u011c\3\2\2\2\u011f\u0122\3\2\2\2\u0120\u011e\3\2\2\2\u0120\u0121"+
		"\3\2\2\2\u0121\u0123\3\2\2\2\u0122\u0120\3\2\2\2\u0123\u0124\7\5\2\2\u0124"+
		"\u016e\3\2\2\2\u0125\u016e\7E\2\2\u0126\u0127\t\4\2\2\u0127\u016e\5\22"+
		"\n\21\u0128\u0129\7\62\2\2\u0129\u016e\5\22\n\20\u012a\u012b\7B\2\2\u012b"+
		"\u016e\5\22\n\17\u012c\u012d\7$\2\2\u012d\u012e\7\3\2\2\u012e\u012f\7"+
		"E\2\2\u012f\u0130\7\4\2\2\u0130\u0131\5\22\n\2\u0131\u0132\7\4\2\2\u0132"+
		"\u0133\5\22\n\2\u0133\u0134\7\4\2\2\u0134\u0135\5\22\n\2\u0135\u0136\7"+
		"\5\2\2\u0136\u016e\3\2\2\2\u0137\u0138\7%\2\2\u0138\u0139\5\22\n\2\u0139"+
		"\u013a\7&\2\2\u013a\u013b\5\22\n\2\u013b\u013c\7\'\2\2\u013c\u013d\5\22"+
		"\n\7\u013d\u016e\3\2\2\2\u013e\u013f\7\3\2\2\u013f\u0140\7\3\2\2\u0140"+
		"\u0145\5\f\7\2\u0141\u0142\7\4\2\2\u0142\u0144\5\f\7\2\u0143\u0141\3\2"+
		"\2\2\u0144\u0147\3\2\2\2\u0145\u0143\3\2\2\2\u0145\u0146\3\2\2\2\u0146"+
		"\u0148\3\2\2\2\u0147\u0145\3\2\2\2\u0148\u0149\7\5\2\2\u0149\u014a\7\6"+
		"\2\2\u014a\u014b\5\16\b\2\u014b\u014c\7\7\2\2\u014c\u014d\5\22\n\2\u014d"+
		"\u014e\7\5\2\2\u014e\u016e\3\2\2\2\u014f\u0150\7)\2\2\u0150\u0151\7\3"+
		"\2\2\u0151\u0156\5\f\7\2\u0152\u0153\7\4\2\2\u0153\u0155\5\f\7\2\u0154"+
		"\u0152\3\2\2\2\u0155\u0158\3\2\2\2\u0156\u0154\3\2\2\2\u0156\u0157\3\2"+
		"\2\2\u0157\u0159\3\2\2\2\u0158\u0156\3\2\2\2\u0159\u015a\7\5\2\2\u015a"+
		"\u015b\7\21\2\2\u015b\u015c\5\22\n\4\u015c\u016e\3\2\2\2\u015d\u015e\7"+
		")\2\2\u015e\u015f\7\3\2\2\u015f\u0164\5\f\7\2\u0160\u0161\7\4\2\2\u0161"+
		"\u0163\5\f\7\2\u0162\u0160\3\2\2\2\u0163\u0166\3\2\2\2\u0164\u0162\3\2"+
		"\2\2\u0164\u0165\3\2\2\2\u0165\u0167\3\2\2\2\u0166\u0164\3\2\2\2\u0167"+
		"\u0168\7\5\2\2\u0168\u0169\7\21\2\2\u0169\u016a\5\22\n\2\u016a\u016b\7"+
		"*\2\2\u016b\u016c\5\22\n\3\u016c\u016e\3\2\2\2\u016d\u00b5\3\2\2\2\u016d"+
		"\u00ba\3\2\2\2\u016d\u00bb\3\2\2\2\u016d\u00bc\3\2\2\2\u016d\u00bd\3\2"+
		"\2\2\u016d\u00c2\3\2\2\2\u016d\u00c6\3\2\2\2\u016d\u00cb\3\2\2\2\u016d"+
		"\u00cf\3\2\2\2\u016d\u00d5\3\2\2\2\u016d\u00e0\3\2\2\2\u016d\u00e7\3\2"+
		"\2\2\u016d\u00ee\3\2\2\2\u016d\u00f5\3\2\2\2\u016d\u00ff\3\2\2\2\u016d"+
		"\u0105\3\2\2\2\u016d\u010b\3\2\2\2\u016d\u0111\3\2\2\2\u016d\u0116\3\2"+
		"\2\2\u016d\u0119\3\2\2\2\u016d\u0125\3\2\2\2\u016d\u0126\3\2\2\2\u016d"+
		"\u0128\3\2\2\2\u016d\u012a\3\2\2\2\u016d\u012c\3\2\2\2\u016d\u0137\3\2"+
		"\2\2\u016d\u013e\3\2\2\2\u016d\u014f\3\2\2\2\u016d\u015d\3\2\2\2\u016e"+
		"\u018f\3\2\2\2\u016f\u0170\f\16\2\2\u0170\u0171\t\5\2\2\u0171\u018e\5"+
		"\22\n\17\u0172\u0173\f\r\2\2\u0173\u0174\t\4\2\2\u0174\u018e\5\22\n\16"+
		"\u0175\u0176\f\f\2\2\u0176\u0177\t\6\2\2\u0177\u018e\5\22\n\r\u0178\u0179"+
		"\f\13\2\2\u0179\u017a\t\7\2\2\u017a\u018e\5\22\n\f\u017b\u017c\f\n\2\2"+
		"\u017c\u017d\t\b\2\2\u017d\u018e\5\22\n\13\u017e\u017f\f\t\2\2\u017f\u0180"+
		"\t\t\2\2\u0180\u018e\5\22\n\n\u0181\u0182\f\6\2\2\u0182\u0183\7(\2\2\u0183"+
		"\u018e\5\22\n\7\u0184\u0185\f\33\2\2\u0185\u0186\7\35\2\2\u0186\u018e"+
		"\7E\2\2\u0187\u0189\f\22\2\2\u0188\u018a\5\20\t\2\u0189\u0188\3\2\2\2"+
		"\u018a\u018b\3\2\2\2\u018b\u0189\3\2\2\2\u018b\u018c\3\2\2\2\u018c\u018e"+
		"\3\2\2\2\u018d\u016f\3\2\2\2\u018d\u0172\3\2\2\2\u018d\u0175\3\2\2\2\u018d"+
		"\u0178\3\2\2\2\u018d\u017b\3\2\2\2\u018d\u017e\3\2\2\2\u018d\u0181\3\2"+
		"\2\2\u018d\u0184\3\2\2\2\u018d\u0187\3\2\2\2\u018e\u0191\3\2\2\2\u018f"+
		"\u018d\3\2\2\2\u018f\u0190\3\2\2\2\u0190\23\3\2\2\2\u0191\u018f\3\2\2"+
		"\2\u0192\u0193\7\20\2\2\u0193\25\3\2\2\2\u0194\u0195\7\21\2\2\u0195\27"+
		"\3\2\2\2\27\34\36\"+9JTfp\u009f\u00a2\u00ac\u00db\u0120\u0145\u0156\u0164"+
		"\u016d\u018b\u018d\u018f";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}