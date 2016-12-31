function script(args)
radsite=subwrd(args,1)
radfname=subwrd(args,2)
output=subwrd(args,3)
mapshapes=subwrd(args,4)
localdate=subwrd(args,5)
localtime=subwrd(args,6)
'set display color white'
'sdfopen '%radfname%''
'set timelab off'
'set grads off'
'set xlab off'
'set ylab off'
'set grid off'
'set mpdraw off'
'set gxout grfill'
'd KDP'
'set rgb 99 245 245 220'
'set line 15 1 1'
'set shpopts 99'
'draw shp c_10nv15.shp'
'color 0 500 2 -kind black->gray->maroon->darkred->red->palevioletred->mediumpurple->cyan->green->yellow->orange->navajowhite->white'
'd KDP'
'xcbar 9.5 9.7 1.5 7.0 -fs 5.0'
'q time'
res = subwrd(result,3)
'draw title ' %localdate%' '%localtime%''
'printim '%output%''

***
