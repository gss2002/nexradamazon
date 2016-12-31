function script(args)
radsite=subwrd(args,1)
radfname=subwrd(args,2)
output=subwrd(args,3)
mapshapes=subwrd(args,4)
localdate=subwrd(args,5)
localtime=subwrd(args,6)
'sdfopen '%radfname%''
'set display color white'
'set rgb 100 0 0 205'
'set background 100'
'c'
'set parea 0.0 9.0 0.0 8.5'
'set timelab off'
'set grads off'
'set xlab off'
'set ylab off'
*'set lat 38.1665 43.5645'
*'set lon -76.4328 -69.2949'
'set grid off'
'set frame off'
'set mpdraw off'
'set mproj scaled'
'set gxout contour'
'set clevs 0'
*'color 0.0 1.0 2.0 -kind (255,255,255)->(255,255,255)'
'd Reflectivity'
'set background 100'
'set rgb 99 245 245 220'
'set line 15 1 1'
'set shpopts 99'
'draw shp c_10nv15.shp'
'printim '%output%''
