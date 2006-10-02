/*
  * JBoss, Home of Professional Open Source
  * Copyright 2005, JBoss Inc., and individual contributors as indicated
  * by the @authors tag. See the copyright.txt in the distribution for a
  * full listing of individual contributors.
  *
  * This is free software; you can redistribute it and/or modify it
  * under the terms of the GNU Lesser General Public License as
  * published by the Free Software Foundation; either version 2.1 of
  * the License, or (at your option) any later version.
  *
  * This software is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  * Lesser General Public License for more details.
  *
  * You should have received a copy of the GNU Lesser General Public
  * License along with this software; if not, write to the Free
  * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
  * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
  */
package org.jboss.aop.annotation.factory.duplicate.ast;

public class AnnotationParser/*@bgen(jjtree)*/implements AnnotationParserTreeConstants, AnnotationParserConstants {/*@bgen(jjtree)*/
  protected JJTAnnotationParserState jjtree = new JJTAnnotationParserState();

  final public ASTStart Start() throws ParseException {
                           /*@bgen(jjtree) Start */
  ASTStart jjtn000 = new ASTStart(JJTSTART);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      Annotation();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 7:
        jj_consume_token(7);
        break;
      case 8:
        jj_consume_token(8);
        break;
      case 0:
        jj_consume_token(0);
        break;
      default:
        jj_la1[0] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
    {if (true) return jjtn000;}
    } catch (Throwable jjte000) {
    if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    if (jjte000 instanceof ParseException) {
      {if (true) throw (ParseException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
    if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
    throw new Error("Missing return statement in function");
  }

  final public void Annotation() throws ParseException {
 /*@bgen(jjtree) Annotation */
  ASTAnnotation jjtn000 = new ASTAnnotation(JJTANNOTATION);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);Token tok;
    try {
      jj_consume_token(9);
      tok = jj_consume_token(IDENTIFIER);
     jjtn000.setIdentifier(tok.image);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 10:
        jj_consume_token(10);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case IDENTIFIER:
        case CHARACTER:
        case STRING:
        case 9:
        case 14:
          if (jj_2_1(2)) {
            MemberValuePairs();
          } else {
            switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
            case IDENTIFIER:
            case CHARACTER:
            case STRING:
            case 9:
            case 14:
              SingleMemberValue();
              break;
            default:
              jj_la1[1] = jj_gen;
              jj_consume_token(-1);
              throw new ParseException();
            }
          }
          break;
        default:
          jj_la1[2] = jj_gen;
          ;
        }
        jj_consume_token(11);
        break;
      default:
        jj_la1[3] = jj_gen;
        ;
      }
    } catch (Throwable jjte000) {
    if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    if (jjte000 instanceof ParseException) {
      {if (true) throw (ParseException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
    if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  final public void SingleMemberValue() throws ParseException {
 /*@bgen(jjtree) SingleMemberValue */
  ASTSingleMemberValue jjtn000 = new ASTSingleMemberValue(JJTSINGLEMEMBERVALUE);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      MemberValue();
    } catch (Throwable jjte000) {
     if (jjtc000) {
       jjtree.clearNodeScope(jjtn000);
       jjtc000 = false;
     } else {
       jjtree.popNode();
     }
     if (jjte000 instanceof RuntimeException) {
       {if (true) throw (RuntimeException)jjte000;}
     }
     if (jjte000 instanceof ParseException) {
       {if (true) throw (ParseException)jjte000;}
     }
     {if (true) throw (Error)jjte000;}
    } finally {
     if (jjtc000) {
       jjtree.closeNodeScope(jjtn000, true);
     }
    }
  }

  final public void MemberValuePairs() throws ParseException {
 /*@bgen(jjtree) MemberValuePairs */
  ASTMemberValuePairs jjtn000 = new ASTMemberValuePairs(JJTMEMBERVALUEPAIRS);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      MemberValuePair();
      label_1:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 12:
          ;
          break;
        default:
          jj_la1[4] = jj_gen;
          break label_1;
        }
        jj_consume_token(12);
        MemberValuePair();
      }
    } catch (Throwable jjte000) {
    if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    if (jjte000 instanceof ParseException) {
      {if (true) throw (ParseException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
    if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  final public void MemberValuePair() throws ParseException {
 /*@bgen(jjtree) MemberValuePair */
  ASTMemberValuePair jjtn000 = new ASTMemberValuePair(JJTMEMBERVALUEPAIR);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);Token specialToken;
    try {
      Identifier();
      jj_consume_token(13);
      MemberValue();
    } catch (Throwable jjte000) {
    if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    if (jjte000 instanceof ParseException) {
      {if (true) throw (ParseException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
    if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  final public void MemberValue() throws ParseException {
  Token tok;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 9:
      Annotation();
      break;
    case 14:
      MemberValueArrayInitializer();
      break;
    case STRING:
      String();
      break;
    case CHARACTER:
      Char();
      break;
    case IDENTIFIER:
      Identifier();
      break;
    default:
      jj_la1[5] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void MemberValueArrayInitializer() throws ParseException {
 /*@bgen(jjtree) MemberValueArrayInitializer */
  ASTMemberValueArrayInitializer jjtn000 = new ASTMemberValueArrayInitializer(JJTMEMBERVALUEARRAYINITIALIZER);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(14);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case IDENTIFIER:
      case CHARACTER:
      case STRING:
      case 9:
      case 14:
        MemberValue();
        label_2:
        while (true) {
          if (jj_2_2(2)) {
            ;
          } else {
            break label_2;
          }
          jj_consume_token(12);
          MemberValue();
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 12:
          jj_consume_token(12);
          break;
        default:
          jj_la1[6] = jj_gen;
          ;
        }
        break;
      default:
        jj_la1[7] = jj_gen;
        ;
      }
      jj_consume_token(15);
    } catch (Throwable jjte000) {
    if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    if (jjte000 instanceof ParseException) {
      {if (true) throw (ParseException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
    if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
  }

  final public void Identifier() throws ParseException {
 /*@bgen(jjtree) Identifier */
    ASTIdentifier jjtn000 = new ASTIdentifier(JJTIDENTIFIER);
    boolean jjtc000 = true;
    jjtree.openNodeScope(jjtn000);Token tok;
    try {
      tok = jj_consume_token(IDENTIFIER);
      jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
       jjtn000.setValue(tok.image);
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  final public void String() throws ParseException {
 /*@bgen(jjtree) String */
    ASTString jjtn000 = new ASTString(JJTSTRING);
    boolean jjtc000 = true;
    jjtree.openNodeScope(jjtn000);Token tok;
    try {
      tok = jj_consume_token(STRING);
      jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
       jjtn000.setValue(tok.image);
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  final public void Char() throws ParseException {
 /*@bgen(jjtree) Char */
    ASTChar jjtn000 = new ASTChar(JJTCHAR);
    boolean jjtc000 = true;
    jjtree.openNodeScope(jjtn000);Token tok;
    try {
      tok = jj_consume_token(CHARACTER);
      jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
       jjtn000.setValue(tok.image);
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  final private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  final private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  final private boolean jj_3R_4() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_6()) {
    jj_scanpos = xsp;
    if (jj_3R_7()) {
    jj_scanpos = xsp;
    if (jj_3R_8()) {
    jj_scanpos = xsp;
    if (jj_3R_9()) {
    jj_scanpos = xsp;
    if (jj_3R_10()) return true;
    }
    }
    }
    }
    return false;
  }

  final private boolean jj_3R_6() {
    if (jj_3R_12()) return true;
    return false;
  }

  final private boolean jj_3_1() {
    if (jj_3R_3()) return true;
    return false;
  }

  final private boolean jj_3R_14() {
    if (jj_scan_token(STRING)) return true;
    return false;
  }

  final private boolean jj_3R_13() {
    if (jj_scan_token(14)) return true;
    return false;
  }

  final private boolean jj_3R_5() {
    if (jj_3R_11()) return true;
    if (jj_scan_token(13)) return true;
    return false;
  }

  final private boolean jj_3_2() {
    if (jj_scan_token(12)) return true;
    if (jj_3R_4()) return true;
    return false;
  }

  final private boolean jj_3R_10() {
    if (jj_3R_11()) return true;
    return false;
  }

  final private boolean jj_3R_9() {
    if (jj_3R_15()) return true;
    return false;
  }

  final private boolean jj_3R_12() {
    if (jj_scan_token(9)) return true;
    return false;
  }

  final private boolean jj_3R_11() {
    if (jj_scan_token(IDENTIFIER)) return true;
    return false;
  }

  final private boolean jj_3R_15() {
    if (jj_scan_token(CHARACTER)) return true;
    return false;
  }

  final private boolean jj_3R_8() {
    if (jj_3R_14()) return true;
    return false;
  }

  final private boolean jj_3R_3() {
    if (jj_3R_5()) return true;
    return false;
  }

  final private boolean jj_3R_7() {
    if (jj_3R_13()) return true;
    return false;
  }

  public AnnotationParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  public boolean lookingAhead = false;
  private boolean jj_semLA;
  private int jj_gen;
  final private int[] jj_la1 = new int[8];
  static private int[] jj_la1_0;
  static {
      jj_la1_0();
   }
   private static void jj_la1_0() {
      jj_la1_0 = new int[] {0x181,0x4268,0x4268,0x400,0x1000,0x4268,0x1000,0x4268,};
   }
  final private JJCalls[] jj_2_rtns = new JJCalls[2];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  public AnnotationParser(java.io.InputStream stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new AnnotationParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public void ReInit(java.io.InputStream stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public AnnotationParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new AnnotationParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public AnnotationParser(AnnotationParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public void ReInit(AnnotationParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error {}
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  final private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }

  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  final public Token getToken(int index) {
    Token t = lookingAhead ? jj_scanpos : token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  final private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.Vector jj_expentries = new java.util.Vector();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      boolean exists = false;
      for (java.util.Enumeration e = jj_expentries.elements(); e.hasMoreElements();) {
        int[] oldentry = (int[])(e.nextElement());
        if (oldentry.length == jj_expentry.length) {
          exists = true;
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              exists = false;
              break;
            }
          }
          if (exists) break;
        }
      }
      if (!exists) jj_expentries.addElement(jj_expentry);
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[16];
    for (int i = 0; i < 16; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 8; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 16; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.elementAt(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  final public void enable_tracing() {
  }

  final public void disable_tracing() {
  }

  final private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 2; i++) {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
          }
        }
        p = p.next;
      } while (p != null);
    }
    jj_rescan = false;
  }

  final private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}
