/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * [Project]
 *       MyTextView
 * [Package]
 *       com.juntian.myth
 * [FileName]
 *       MTextView.java
 * [History]
 *       Version          Date              Author               Record
 *--------------------------------------------------------------------------------------
 *       1.0.0        2013-12-03         myth                 Create
 *       1.1.0        2014-06-11         myth                 update
 **************************************************************************************/
package cn.chono.yopper.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
/**
 * @文件名：MeTextView.java
 * @包名：com.juntian.myth.texttest
 * @作者：小贾  xiaojia1680@foxmail.com
 * @创建时间：2015-1-13 下午4:04:29
 * @描述： 自定义textview
 */
public class JustifyTextView extends View {
    private Paint paint;
    private CheckChar cc;

    public JustifyTextView(Context context) {
        this(context, null);
    }

    public JustifyTextView(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public JustifyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ini();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (TextTab t : cc.tt) {
            float x = 0f;
            for (int i = 0; i < t.s.length(); i++) {
                canvas.drawText(t.s, i, i + 1, x, t.t, paint);
                if (t.b && i < 2) {
                    x += paint.measureText(t.s, i, i + 1);
                } else {
                    x += paint.measureText(t.s, i, i + 1) + t.d;
                }
            }
        }
    }

    private void ini() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        cc = new CheckChar(paint);
    }

    public void ini(String desc, int ttw, int tts) {
        float td = tts * 0.35f;// 行间距
        float ttd = tts + td;// 字体高度
        paint.setTextSize(tts);
        cc.ini(desc, ttw, tts, ttd);
        invalidate();
        int tth = (int) (cc.tt.size() * ttd + td / 2);// view高度
        getLayoutParams().height = tth;
    }

    private class TextTab {
        private String s;// 字符串
        private float d;// 字间距
        private float t;// 行间距
        private boolean b;// 段落首行

        public TextTab(String s, float d, float t, boolean b) {
            super();
            this.s = s;
            this.d = d;
            this.t = t;
            this.b = b;
        }
    }

    private class CheckChar {
        private String[] sts = new String[] { "\n", "&nbsp;", "", "\u3000\u3000" };
        private char[] cha = new char[] { 0xff00, 0x20, 0xa0, 0x09, 0x0d, 0x3000, 0xff5f };
        private char[] chs = new char[] { 0x2f, 0x3a, 0x40, 0x5b, 0x60, 0x7b, 0x20, 0x30, 0x39, 0x41, 0x5a, 0x61, 0x7a, 0x7f, 0x3001, 0x3002 };
        private Paint paint;
        private List<TextTab> tt;
        private int tw, ts;
        private float tb;

        public CheckChar(Paint paint) {
            this.paint = paint;
            tt = new ArrayList<TextTab>();
        }

        public void ini(String s, int tw, int ts, float tb) {
            this.tw = tw;
            this.ts = ts;
            this.tb = tb;
            tt.clear();
            String[] cos = convertSBC2DBC(s).split(sts[0]);
            for (String c : cos) {
                charset2(tt, c, 0, c.length());
            }
        }

        private String convertSBC2DBC(String co) {
            char[] ch = co.toCharArray();
            StringBuilder sb = new StringBuilder();
            int offset = cha[0] - cha[1];
            for (char c : ch) {
                // 可忽略字符
                if (c == cha[2] || c == cha[3] || c == cha[4]) {
                    continue;
                } else
                    // ASCII全角转半角空格
                    if (c == cha[5]) {
                        c = cha[1];
                    } else
                        // ASCII全角转半角字符
                        if (c > cha[0] && c < cha[6]) {
                            c -= offset;
                        }
                sb.append(c);
            }

            boolean first = true;
            String[] cos = sb.toString().replaceAll(sts[1], sts[2]).split(sts[0]);
            sb.delete(0, sb.length());
            for (String s : cos) {
                s = s.trim();
                if (s.length() == 0) {
                    continue;
                }
                if (first) {
                    first = false;
                } else {
                    sb.append(sts[0]);
                }
                sb.append(sts[3] + s);
            }
            return sb.toString().trim();
        }

        private void charset2(List<TextTab> tt, String s, int b, int len) {
            if (b < len) {
                int e = b + 1;
                float div = tw - paint.measureText(s, b, e);
                while (div / ts > 2 && e < len) {
                    e++;
                    div = tw - paint.measureText(s, b, e);
                }
                while (div / ts > 1 && e < len) {
                    char c = s.charAt(e);
                    if (c == chs[6] || (c > chs[6] && c < chs[7]) || (c > chs[8] && c < chs[9]) || (c > chs[10] && c < chs[11]) || (c > chs[12] && c < chs[13])) {
                        e++;
                    } else {
                        break;
                    }
                    div = tw - paint.measureText(s, b, e);
                }
                while (div / ts > 1 && e < len) {
                    char c = s.charAt(e);
                    if (c > chs[0] && c < chs[1]) {
                        e++;
                    } else {
                        break;
                    }
                    div = tw - paint.measureText(s, b, e);
                }
                while (div / ts > 1 && e < len) {
                    char c = s.charAt(e);
                    if ((c > chs[2] && c < chs[3]) || (c > chs[4] && c < chs[5])) {
                        e++;
                    } else {
                        break;
                    }
                    div = tw - paint.measureText(s, b, e);
                }
                if (div / ts < 1) {
                    char c = s.charAt(e);
                    char cc = s.charAt(e - 1);
                    while ((c > chs[0] && c < chs[1]) && (cc > chs[0] && cc < chs[1])) {
                        e--;
                        c = s.charAt(e);
                        cc = s.charAt(e - 1);
                        div = tw - paint.measureText(s, b, e);
                    }
                    while (((c > chs[2] && c < chs[3]) || (c > chs[4] && c < chs[5])) && ((cc > chs[2] && cc < chs[3]) || (cc > chs[4] && cc < chs[5]))) {
                        e--;
                        c = s.charAt(e);
                        cc = s.charAt(e - 1);
                        div = tw - paint.measureText(s, b, e);
                    }
                }

                String sco = s.substring(b, e).trim();
                float tc = (tt.size() + 1) * tb;
                if (e == len) {
                    if (tt.size() == 0) {
                        div = 0;
                    } else {
                        float f = 0;
                        for (TextTab t : tt) {
                            f += t.d;
                        }
                        div = f / tt.size();
                    }
                } else if (b == 0) {
                    div = div / (sco.length() - 3);
                } else {
                    div = div / (sco.length() - 1);
                }
                tt.add(new TextTab(sco, div, tc, b == 0));
                charset2(tt, s, e, len);
            }
        }
    }
}
