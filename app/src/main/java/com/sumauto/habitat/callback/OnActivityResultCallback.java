package com.sumauto.habitat.callback;

import android.content.Intent;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/04/19 5.6.6 
 */
public interface OnActivityResultCallback {
     void onActivityResult(int requestCode, int resultCode, Intent data);
}
