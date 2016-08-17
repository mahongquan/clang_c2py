# -*- coding: utf-8 -*-
import sys
import clang.cindex
from clang.cindex import Config
Config.set_library_path(r"D:\LLVM\bin")
index = clang.cindex.Index.create()
fn="p/src/global.c"
f=open(fn,"r")
fc=f.readlines()
fc.append('')
f.close()
tu = index.parse(fn)
def showSource(cursor):
	#print(fc,len(fc))
	start=[cursor.extent.start.line-1,cursor.extent.start.column-1]
	end=[cursor.extent.end.line-1,cursor.extent.end.column-1]
	#print(start,end)
	fc1=fc[start[0]:]
	end[0]=end[0]-start[0]
	start[0]=0
	fc1[0]=fc1[0][start[1]:]
	# if cursor.kind==clang.cindex.CursorKind.FUNCTION_DECL:
	# 	print(fc1)
	# 	input("hi")
	#print(start,end)
	if end[0]==start[0]:
		end[1]=end[1]-start[1]
		fc1[0]=fc1[0][:end[1]+1]
		return fc1[0]
	else:
		fc1[end[0]]=fc1[end[0]][:end[1]]
		return fc1[0:end[0]+1]
def showToken(node):
    r=[]
    ts=node.get_tokens()
    for t in ts:
        r.append(t.spelling)
    return " ".join(r)
def showArgument(node):
    r=[]
    ts=node.get_arguments()
    for t in ts:
        r.append(t.spelling)
    r=",".join(r)
    return "(%s)" % r
def visit_COMPOUND_STMT(cursor):
	print("visit_COMPOUND_STMT")
	cs=[]
	for c in cursor.get_children():
		cs.append(c)
	r=[]
	r.append("{")
	for c in cs:
		r.append(visit(c))
	r.append("}")
	return r
def visit_PARM_DECL(cursor):
	print('param')
	print(showToken(cursor))
	print(cursor.type,cursor.result_type.spelling)
	return [cursor.type.spelling,cursor.displayname]
	return showToken(cursor)
def visit_FUNCTION_DECL(cursor):
	cs=[]
	for c in cursor.get_children():
		cs.append(c)
	r=[cursor.result_type.spelling,cursor.spelling]
	r.append("(")
	n=len(cs)
	i=0
	for c in cs[:-1]:
		r.append(visit(c))# param
		if i==n-2:
			pass
		else:
			r.append(",")
		i+=1
	r.append(")")
	if len(cs)-1>0:
		r.append(visit(cs[len(cs)-1]))#visit_COMPOUND_STMT(cs[len(cs)-1]))
	return r
def visit_RETURN_STMT(cursor):
	print("visit return")
	cs=[]
	for c in cursor.get_children():
		cs.append(c)
	r=["return"]
	for c in cs:
		r.append(visit(c))
	return r
def visit_INTEGER_LITERAL(cursor):
	print("visit_INTEGER_LITERAL")
	r=[]
	for t in cursor.get_tokens():
		r.append(t.spelling)
	return r#cursor.displayname#showToken(cursor)
def visit_unknown(cursor):
	r=[]
	for c in cursor.get_children():
		r.append(visit(c))
	return r
def visit_UNARY_OPERATOR(cursor):# a++;
	r=[]
	for t in cursor.get_tokens():
		r.append(t.spelling)
	return r#cursor.displayname#showToken(cursor)
def visit_TRANSLATION_UNIT(cursor):
	r=[]
	for c in cursor.get_children():
		r.append(visit(c))
	return r
def visit_DECL_REF_EXPR(cursor):#a++
	r=[]
	for t in cursor.get_tokens():
		r.append(t.spelling)
	return r#cursor.displayname#showToken(cursor)
def visit_VAR_DECL(cursor):#a++
	r=[]
	for t in cursor.get_tokens():
		r.append(t.spelling)
	return r#cursor.displayname#showToken(cursor)
def visit(cursor):
	print("=cursor info===============================================")
	print(cursor.kind)
	print(showSource(cursor))
	for c in cursor.get_children():
		print(c.kind,showSource(c))
	print("=end cursor info============================================")
	
	if cursor.kind==clang.cindex.CursorKind.TRANSLATION_UNIT:
		return visit_TRANSLATION_UNIT(cursor)
	elif cursor.kind==clang.cindex.CursorKind.FUNCTION_DECL:
		return visit_FUNCTION_DECL(cursor)
	elif cursor.kind==clang.cindex.CursorKind.PARM_DECL:
		return visit_PARM_DECL(cursor)
	elif cursor.kind==clang.cindex.CursorKind.COMPOUND_STMT:
		return visit_COMPOUND_STMT(cursor)
	elif cursor.kind==clang.cindex.CursorKind.RETURN_STMT:
		return visit_RETURN_STMT(cursor)
	elif cursor.kind==clang.cindex.CursorKind.INTEGER_LITERAL:
		return visit_INTEGER_LITERAL(cursor)
	elif cursor.kind==clang.cindex.CursorKind.UNARY_OPERATOR:
		return visit_UNARY_OPERATOR(cursor)
	elif cursor.kind==clang.cindex.CursorKind.DECL_REF_EXPR:
		return visit_DECL_REF_EXPR(cursor)
	elif cursor.kind==clang.cindex.CursorKind.VAR_DECL:
		return visit_VAR_DECL(cursor)
	else:
		return visit_unknown(cursor)
def showResult(l):
	r=[]
	for c in l:
		#print(c,type(c))
		if type(c)==list:
			r.append(showResult(c))
		elif c==None:
			r.append("")
		else:
			r.append(c)
	return " ".join(r)
print(dir(tu.cursor))	
res=visit(tu.cursor)
print("res",res)
print(showResult(res))
# print(dir(tu.cursor))
# func1=None
# for t in tu.cursor.walk_preorder():
# 	#extend  源码范围
# 	#location 源码位置
# 	print(t.access_specifier,t.kind,t.spelling,t.displayname)#,t.data,dir(t.data))#t.type)#t.location)
# 	if t.kind==clang.cindex.CursorKind.FUNCTION_DECL:
# 		print(t.displayname)
# 		func1=t
# 	if t.kind==clang.cindex.CursorKind.RETURN_STMT:
# 		print(showToken(t))