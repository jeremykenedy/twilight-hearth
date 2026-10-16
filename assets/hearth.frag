#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES fire;
uniform sampler2D diffuseMap;
uniform sampler2D normalMap;
uniform sampler2D woodMap;
uniform sampler2D woodNormal;
uniform mat4 videoMatrix;
uniform vec2 resolution;
uniform float time;
uniform float material;
uniform float light;
uniform float fireExposure;
varying vec2 uv;
void main(){
 vec2 p=(uv*2.-1.)*vec2(resolution.x/resolution.y,1.);
 // The entire architectural frame drifts slowly, including its fixed edges.
 p+=vec2(.009*sin(time*.013),.006*cos(time*.017));
 vec2 box=vec2(1.18,.66375);
 vec2 q=abs(p)-box;
 float edge=max(q.x,q.y);
 float pulse=.92+.045*sin(time*2.7)+.025*sin(time*5.3);
 vec3 color;
 if(edge<0.){
   vec2 f=(p/box+1.)*.5;
   vec4 sampleUv=videoMatrix*vec4(f,0.,1.);
   color=texture2D(fire,sampleUv.xy).rgb;
   float luminance=dot(color,vec3(.299,.587,.114));
   float flame=smoothstep(.22,.85,luminance)*smoothstep(.03,.25,color.r-color.b);
   color*=mix(1.,fireExposure,flame);
   color*=.68+light*.36;
   color*=smoothstep(0.,.028,-edge);
 }else{
   vec2 surfaceUv=p*.55;
   vec3 base=texture2D(diffuseMap,surfaceUv).rgb;
   vec3 normal=normalize(texture2D(normalMap,surfaceUv).rgb*2.-1.);
   if(material>1.5){
     float value=dot(base,vec3(.299,.587,.114));
     base=value*vec3(.46,.5,.55);
   }
   float cap=step(box.y+.12,p.y)*step(p.y,box.y+.24)*step(abs(p.x),box.x+.22);
   float leg=step(box.x+.10,abs(p.x))*step(abs(p.x),box.x+.22)*step(-box.y-.11,p.y)*step(p.y,box.y+.12);
   if(material<1.5 && max(cap,leg)>.5){
     vec2 timberUv=cap>.5?p.yx*vec2(1.5,.55):p*vec2(1.5,.55);
     base=texture2D(woodMap,timberUv).rgb*.72;
     normal=normalize(texture2D(woodNormal,timberUv).rgb*2.-1.);
     if(cap>.5)base*=.78+.22*smoothstep(box.y+.12,box.y+.145,p.y);
   }
   float relief=.76+.24*max(0.,dot(normal,normalize(vec3(-.4,.7,1.))));
   base*=relief;
   float reveal=max(q.x,q.y);
   if(reveal<.028)base=vec3(.027,.025,.023)+.055*exp(-abs(reveal-.014)*190.);
   else if(reveal<.075)base*=.40+.5*(reveal-.028)/.047;
   else if(reveal<.086)base*=1.15;
   float room=.24+light*.48;
   float spill=exp(-length(max(q,vec2(0.)))*3.4)*.34*pulse*fireExposure;
   vec3 toFire=normalize(vec3(-p.x,-p.y,.8));
   color=base*room+base*vec3(1.,.38,.11)*spill*(.6+.4*max(0.,dot(normal,toFire)));
   if(p.y< -box.y-.09 && p.y> -box.y-.17 && abs(p.x)<box.x+.22){
     color*=.78;
     color+=vec3(.055,.018,.004)*pulse*exp(-abs(p.x)*.9);
   }
 }
 color*=1.-.16*pow(length((uv-.5)*1.25),2.);
 gl_FragColor=vec4(color,1.);
}
