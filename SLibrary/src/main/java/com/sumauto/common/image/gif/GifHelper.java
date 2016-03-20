package com.sumauto.common.image.gif;

import android.graphics.Bitmap;

import java.io.InputStream;
import java.util.Vector;

/**
 * Created by Lincoln on 2016/3/16.
 */
public class GifHelper {
    public static final int STATUS_OK           = 0;
    public static final int STATUS_FORMAT_ERROR = 1;
    public static final int STATUS_OPEN_ERROR   = 2;
    protected int         status;
    protected InputStream in;
    protected int         width;
    protected int         height;
    protected boolean     gctFlag;
    protected int         gctSize;
    protected int loopCount = 1;
    protected int[]   gct;
    protected int[]   lct;
    protected int[]   act;
    protected int     bgIndex;
    protected int     bgColor;
    protected int     lastBgColor;
    protected int     pixelAspect;
    protected boolean lctFlag;
    protected boolean interlace;
    protected int     lctSize;
    protected int     ix;
    protected int     iy;
    protected int     iw;
    protected int     ih;
    protected int     lrx;
    protected int     lry;
    protected int     lrw;
    protected int     lrh;
    protected Bitmap  image;
    protected Bitmap  lastImage;
    protected int     frameindex   = 0;
    protected byte[]  block        = new byte[256];
    protected int     blockSize    = 0;
    protected int     dispose      = 0;
    protected int     lastDispose  = 0;
    protected boolean transparency = false;
    protected int     delay        = 0;
    protected int transIndex;
    protected static final int MaxStackSize = 4096;
    protected short[]          prefix;
    protected byte[]           suffix;
    protected byte[]           pixelStack;
    protected byte[]           pixels;
    protected Vector<GifFrame> frames;
    protected int              frameCount;

    public GifHelper() {
    }

    public int getFrameindex() {
        return this.frameindex;
    }

    public void setFrameindex(int frameindex) {
        this.frameindex = frameindex;
        if (frameindex > this.frames.size() - 1) {
            boolean frameindex1 = false;
        }

    }

    public int getWidth() {
        return this.width;
    }

    public int getHeigh() {
        return this.height;
    }

    public int getDelay(int n) {
        this.delay = -1;
        if (n >= 0 && n < this.frameCount) {
            this.delay = ((GifFrame) this.frames.elementAt(n)).delay;
        }

        return this.delay;
    }

    public int getFrameCount() {
        return this.frameCount;
    }

    public Bitmap getImage() {
        return this.getFrame(0);
    }

    public int getLoopCount() {
        return this.loopCount;
    }

    protected void setPixels() {
        int[] dest = new int[this.width * this.height];
        int pass;
        int iline;
        int i;
        int line;
        if (this.lastDispose > 0) {
            if (this.lastDispose == 3) {
                pass = this.frameCount - 2;
                if (pass > 0) {
                    this.lastImage = this.getFrame(pass - 1);
                }
                else {
                    this.lastImage = null;
                }
            }

            if (this.lastImage != null) {
                this.lastImage.getPixels(dest, 0, this.width, 0, 0, this.width, this.height);
                if (this.lastDispose == 2) {
                    pass = 0;
                    if (!this.transparency) {
                        pass = this.lastBgColor;
                    }

                    for (int inc = 0; inc < this.lrh; ++inc) {
                        iline = (this.lry + inc) * this.width + this.lrx;
                        i = iline + this.lrw;

                        for (line = iline; line < i; ++line) {
                            dest[line] = pass;
                        }
                    }
                }
            }
        }

        pass = 1;
        byte var13 = 8;
        iline = 0;

        for (i = 0; i < this.ih; ++i) {
            line = i;
            if (this.interlace) {
                if (iline >= this.ih) {
                    ++pass;
                    switch (pass) {
                        case 2:
                            iline = 4;
                            break;
                        case 3:
                            iline = 2;
                            var13 = 4;
                            break;
                        case 4:
                            iline = 1;
                            var13 = 2;
                    }
                }

                line = iline;
                iline += var13;
            }

            line += this.iy;
            if (line < this.height) {
                int k = line * this.width;
                int dx = k + this.ix;
                int dlim = dx + this.iw;
                if (k + this.width < dlim) {
                    dlim = k + this.width;
                }

                for (int sx = i * this.iw; dx < dlim; ++dx) {
                    int index = this.pixels[sx++] & 255;
                    int c = this.act[index];
                    if (c != 0) {
                        dest[dx] = c;
                    }
                }
            }
        }

        this.image = Bitmap.createBitmap(dest, this.width, this.height, Bitmap.Config.ARGB_4444);
    }

    public Bitmap getFrame(int n) {
        Bitmap im = null;
        if (n >= 0 && n < this.frameCount) {
            im = ((GifFrame) this.frames.elementAt(n)).image;
        }

        return im;
    }

    public Bitmap nextBitmap() {
        ++this.frameindex;
        if (this.frameindex > this.frames.size() - 1) {
            this.frameindex = 0;
        }

        return ((GifFrame) this.frames.elementAt(this.frameindex)).image;
    }

    public int nextDelay() {
        return ((GifFrame) this.frames.elementAt(this.frameindex)).delay;
    }

    public int read(InputStream is) {
        this.init();
        if (is != null) {
            this.in = is;
            this.readHeader();
            if (!this.err()) {
                this.readContents();
                if (this.frameCount < 0) {
                    this.status = 1;
                }
            }
        }
        else {
            this.status = 2;
        }

        try {
            is.close();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return this.status;
    }

    protected void decodeImageData() {
        byte NullCode = -1;
        int npix = this.iw * this.ih;
        if (this.pixels == null || this.pixels.length < npix) {
            this.pixels = new byte[npix];
        }

        if (this.prefix == null) {
            this.prefix = new short[4096];
        }

        if (this.suffix == null) {
            this.suffix = new byte[4096];
        }

        if (this.pixelStack == null) {
            this.pixelStack = new byte[4097];
        }

        int data_size = this.read();
        int clear = 1 << data_size;
        int end_of_information = clear + 1;
        int available = clear + 2;
        int old_code = NullCode;
        int code_size = data_size + 1;
        int code_mask = (1 << code_size) - 1;

        int code;
        for (code = 0; code < clear; ++code) {
            this.prefix[code] = 0;
            this.suffix[code] = (byte) code;
        }

        int bi = 0;
        int pi = 0;
        int top = 0;
        int first = 0;
        int count = 0;
        int bits = 0;
        int datum = 0;
        int i = 0;

        while (i < npix) {
            if (top == 0) {
                if (bits < code_size) {
                    if (count == 0) {
                        count = this.readBlock();
                        if (count <= 0) {
                            break;
                        }

                        bi = 0;
                    }

                    datum += (this.block[bi] & 255) << bits;
                    bits += 8;
                    ++bi;
                    --count;
                    continue;
                }

                code = datum & code_mask;
                datum >>= code_size;
                bits -= code_size;
                if (code > available || code == end_of_information) {
                    break;
                }

                if (code == clear) {
                    code_size = data_size + 1;
                    code_mask = (1 << code_size) - 1;
                    available = clear + 2;
                    old_code = NullCode;
                    continue;
                }

                if (old_code == NullCode) {
                    this.pixelStack[top++] = this.suffix[code];
                    old_code = code;
                    first = code;
                    continue;
                }

                int in_code = code;
                if (code == available) {
                    this.pixelStack[top++] = (byte) first;
                    code = old_code;
                }

                while (code > clear) {
                    this.pixelStack[top++] = this.suffix[code];
                    code = this.prefix[code];
                }

                first = this.suffix[code] & 255;
                if (available >= 4096) {
                    break;
                }

                this.pixelStack[top++] = (byte) first;
                this.prefix[available] = (short) old_code;
                this.suffix[available] = (byte) first;
                ++available;
                if ((available & code_mask) == 0 && available < 4096) {
                    ++code_size;
                    code_mask += available;
                }

                old_code = in_code;
            }

            --top;
            this.pixels[pi++] = this.pixelStack[top];
            ++i;
        }

        for (i = pi; i < npix; ++i) {
            this.pixels[i] = 0;
        }

    }

    protected boolean err() {
        return this.status != 0;
    }

    public void init() {
        this.status = 0;
        this.frameCount = 0;
        this.frames = new Vector();
        this.gct = null;
        this.lct = null;
    }

    protected int read() {
        int curByte = 0;

        try {
            curByte = this.in.read();
        } catch (Exception var3) {
            this.status = 1;
        }

        return curByte;
    }

    protected int readBlock() {
        this.blockSize = this.read();
        int n = 0;
        if (this.blockSize > 0) {
            int e1;
            try {
                for (boolean e = false; n < this.blockSize; n += e1) {
                    e1 = this.in.read(this.block, n, this.blockSize - n);
                    if (e1 == -1) {
                        break;
                    }
                }
            } catch (Exception var3) {
                var3.printStackTrace();
            }

            if (n < this.blockSize) {
                this.status = 1;
            }
        }

        return n;
    }

    protected int[] readColorTable(int ncolors) {
        int nbytes = 3 * ncolors;
        int[] tab = null;
        byte[] c = new byte[nbytes];
        int n = 0;

        try {
            n = this.in.read(c);
        } catch (Exception var11) {
            var11.printStackTrace();
        }

        if (n < nbytes) {
            this.status = 1;
        }
        else {
            tab = new int[256];
            int i = 0;

            int r;
            int g;
            int b;
            for (int j = 0; i < ncolors; tab[i++] = -16777216 | r << 16 | g << 8 | b) {
                r = c[j++] & 255;
                g = c[j++] & 255;
                b = c[j++] & 255;
            }
        }

        return tab;
    }

    protected void readContents() {
        boolean done = false;

        while (!done && !this.err()) {
            int code = this.read();
            switch (code) {
                case 0:
                    break;
                case 33:
                    code = this.read();
                    switch (code) {
                        case 249:
                            this.readGraphicControlExt();
                            continue;
                        case 255:
                            this.readBlock();
                            String app = "";

                            for (int i = 0; i < 11; ++i) {
                                app = app + (char) this.block[i];
                            }

                            if (app.equals("NETSCAPE2.0")) {
                                this.readNetscapeExt();
                            }
                            else {
                                this.skip();
                            }
                            continue;
                        default:
                            this.skip();
                            continue;
                    }
                case 44:
                    this.readImage();
                    break;
                case 59:
                    done = true;
                    break;
                default:
                    this.status = 1;
            }
        }

    }

    protected void readGraphicControlExt() {
        this.read();
        int packed = this.read();
        this.dispose = (packed & 28) >> 2;
        if (this.dispose == 0) {
            this.dispose = 1;
        }

        this.transparency = (packed & 1) != 0;
        this.delay = this.readShort() * 10;
        this.transIndex = this.read();
        this.read();
    }

    protected void readHeader() {
        StringBuffer id = new StringBuffer();

        for (int i = 0; i < 6; ++i) {
            id.append((char) this.read());
        }

        if (!id.toString().startsWith("GIF")) {
            this.status = 1;
        }
        else {
            this.readLSD();
            if (this.gctFlag && !this.err()) {
                this.gct = this.readColorTable(this.gctSize);
                this.bgColor = this.gct[this.bgIndex];
            }

        }
    }

    protected void readImage() {
        this.ix = this.readShort();
        this.iy = this.readShort();
        this.iw = this.readShort();
        this.ih = this.readShort();
        int packed = this.read();
        this.lctFlag = (packed & 128) != 0;
        this.interlace = (packed & 64) != 0;
        this.lctSize = 2 << (packed & 7);
        if (this.lctFlag) {
            this.lct = this.readColorTable(this.lctSize);
            this.act = this.lct;
        }
        else {
            this.act = this.gct;
            if (this.bgIndex == this.transIndex) {
                this.bgColor = 0;
            }
        }

        int save = 0;
        if (this.transparency) {
            save = this.act[this.transIndex];
            this.act[this.transIndex] = 0;
        }

        if (this.act == null) {
            this.status = 1;
        }

        if (!this.err()) {
            this.decodeImageData();
            this.skip();
            if (!this.err()) {
                ++this.frameCount;
                this.image = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_4444);
                this.setPixels();
                this.frames.addElement(new GifFrame(this.image, this.delay));
                if (this.transparency) {
                    this.act[this.transIndex] = save;
                }

                this.resetFrame();
            }
        }
    }

    protected void readLSD() {
        this.width = this.readShort();
        this.height = this.readShort();
        int packed = this.read();
        this.gctFlag = (packed & 128) != 0;
        this.gctSize = 2 << (packed & 7);
        this.bgIndex = this.read();
        this.pixelAspect = this.read();
    }

    protected void readNetscapeExt() {
        do {
            this.readBlock();
            if (this.block[0] == 1) {
                int b1 = this.block[1] & 255;
                int b2 = this.block[2] & 255;
                this.loopCount = b2 << 8 | b1;
            }
        } while (this.blockSize > 0 && !this.err());

    }

    protected int readShort() {
        return this.read() | this.read() << 8;
    }

    protected void resetFrame() {
        this.lastDispose = this.dispose;
        this.lrx = this.ix;
        this.lry = this.iy;
        this.lrw = this.iw;
        this.lrh = this.ih;
        this.lastImage = this.image;
        this.lastBgColor = this.bgColor;
        this.dispose = 0;
        this.transparency = false;
        this.delay = 0;
        this.lct = null;
    }

    protected void skip() {
        do {
            this.readBlock();
        } while (this.blockSize > 0 && !this.err());

    }

    private class GifFrame {
        public Bitmap image;
        public int    delay;

        public GifFrame(Bitmap im, int del) {
            this.image = im;
            this.delay = del;
        }
    }
}
