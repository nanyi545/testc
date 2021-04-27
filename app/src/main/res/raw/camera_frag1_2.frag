#extension GL_OES_EGL_image_external : require
//必须 写的 固定的  意思   用采样器
//所有float类型数据的精度是lowp
precision mediump float;
varying vec2 aCoord;
//采样器  uniform static
uniform samplerExternalOES vTexture;
void main(){
//Opengl 自带函数
    vec4 rgba = texture2D(vTexture,aCoord);

    // 尽量不要用 if ---> 用step优化
    if( aCoord.x>0.5 ){
        gl_FragColor=rgba;
    } else {
        vec2 pos2 = vec2(aCoord.x+0.5 , aCoord.y);
        vec4 c2 = texture2D(vTexture , pos2);
        float grayColorR = ( (c2.r + c2.g + c2.b) / 3.0 ) ;
        gl_FragColor = vec4(rgba.r,rgba.g,rgba.b, grayColorR);
    }




}