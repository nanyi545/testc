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

    // 尽量不要用 if
//    if( aCoord.x>0.5 ){
//        float color=(rgba.r + rgba.g + rgba.b) / 3.0;
//        vec4 tempColor=vec4(color,color,color,1);
//        gl_FragColor=tempColor;
//    } else {
//        gl_FragColor=rgba;
//    }


    // 以下和if等效
    float tx = step(aCoord.x, 0.5);
    float grayColorR = (rgba.r + rgba.g + rgba.b) / 3.0;
    float originR = rgba.r;
    float originG = rgba.g;
    float originB = rgba.b;
    float r = grayColorR * tx + originR * (1.0-tx);
    float g = grayColorR * tx + originG * (1.0-tx);
    float b = grayColorR * tx + originB * (1.0-tx);
    vec4 tempColor=vec4(r,g,b,1);
    gl_FragColor=tempColor;



}