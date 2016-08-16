#!/usr/bin/env python
import sys
import clang.cindex
from clang.cindex import Config
Config.set_library_path(r"D:\LLVM\bin")

def showToken(node):
    r=[]
    ts=node.get_tokens()
    for t in ts:
        r.append(t.spelling)
    return r
index = clang.cindex.Index.create()
#print(dir(index))
tu = index.parse("p/src/main.c")
#showToken(tu.cursor)
# print(dir(tu))
# for i in tu.get_includes():
# 	print(i)
#print(tu.codeComplete())
print(dir(tu.cursor))
for t in tu.cursor.walk_preorder():
	#print(t.kind,t.spelling,t.displayname)#,t.data,dir(t.data))#t.type)#t.location)
	print(t.spelling,t.kind,showToken(t))