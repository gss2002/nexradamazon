function script(args)
radsite=subwrd(args,1)
radfname=subwrd(args,2)
output=subwrd(args,3)
mapshapes=subwrd(args,4)
localdate=subwrd(args,5)
localtime=subwrd(args,6)
bmoutput=subwrd(args,7)
'sdfopen '%radfname%''
'set parea 0.0 9.0 0.0 8.5'
'set timelab off'
'set grads off' 
'set xlab off'
'set ylab off'
'set grid off'
'set mpdraw off'
'set frame off'
'set mproj scaled'
'set gxout grfill'
'color 0.5 94 0.5 -kind (16,78,139)->(0,238,238)->(0,139,0)->(127,255,0)->(255,255,0)->(255,127,0)->(255,0,0)->(139,0,0)->(255,0,255)->(145,44,238)->(255,228,220)->(255,255,255)->(255,255,255)'
'd Reflectivity'
'xcbar 9.5 9.7 1.5 7.0 -fs 10'
'q time' 
res = subwrd(result,3)
'draw title ' %localdate%' '%localtime%'' 
'printim '%output%' -b '%bmoutput%' -t 0'
