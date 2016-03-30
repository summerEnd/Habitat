package com.sumauto.habitat.widget;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.NumberPicker;

import com.sumauto.habitat.R;

import java.util.Arrays;

public class PickCity extends Dialog {
    private NumberPicker provincePicker;
    private NumberPicker cityPicker;
    private OnSelectListener onSelectListener;
    private String provinces[];

    public PickCity(Context context) {
        super(context);
        setContentView(R.layout.city_picker);
        initialize();
    }

    private void initialize() {
        provincePicker = (NumberPicker) findViewById(R.id.province);
        cityPicker = (NumberPicker) findViewById(R.id.city);
        cityPicker.setWrapSelectorWheel(false);
        findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSelectListener != null) {
                    onSelectListener.onSelect(provincePicker.getDisplayedValues()[provincePicker.getValue()], cityPicker.getDisplayedValues()[cityPicker.getValue()]);
                    dismiss();
                }

            }
        });

        findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        displayProvince();
    }

    void displayProvince() {
        provinces = new String[citys.length];
        int maxCityNumbers = 0;
        for (int i = 0; i < provinces.length; i++) {
            provinces[i] = citys[i][0];
            maxCityNumbers = Math.max(citys[i].length, maxCityNumbers);
        }
        String[] maxCityArray = new String[maxCityNumbers];
        String[] firstCityArray = citys[0];
        for (int i = 0; i < maxCityArray.length; i++) {
            if (i < firstCityArray.length - 1) {
                //第一个是省
                maxCityArray[i] = firstCityArray[i + 1];
            } else {
                maxCityArray[i] = "";
            }

        }

        provincePicker.setMaxValue(provinces.length - 1);
        provincePicker.setMinValue(0);
        provincePicker.setDisplayedValues(provinces);
        cityPicker.setMinValue(0);
        cityPicker.setMaxValue(maxCityNumbers - 1);
        cityPicker.setDisplayedValues(maxCityArray);

        displayProvince(0);

        provincePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                displayProvince(newVal);
            }
        });
    }

    void displayProvince(int index) {
        String newCityArray[] = citys[index];
        if (newCityArray.length > 1) {
            newCityArray = Arrays.copyOfRange(newCityArray, 1, newCityArray.length);
        } else {
            newCityArray = new String[]{"", ""};
        }
        String displayValues[] = cityPicker.getDisplayedValues();
        int cityNumber = newCityArray.length;
        if (cityNumber < displayValues.length) {
            for (int i = 0; i < displayValues.length; i++) {
                if (i < cityNumber) {
                    displayValues[i] = newCityArray[i];
                } else {
                    displayValues[i] = "";
                }
            }

        }
        cityPicker.setMinValue(0);
        cityPicker.setValue(0);
        cityPicker.setDisplayedValues(displayValues);
        cityPicker.setMaxValue(newCityArray.length - 1);
        cityPicker.invalidate();
    }


    public PickCity setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
        return this;
    }


    public interface OnSelectListener {
        void onSelect(String province, String city);
    }

    //直辖市 4
    public static String[][] citys = new String[][]{{"北京市", "西城区", "东城区", "宣武区", "崇文区", "海淀区", "朝阳区", "丰台区", "石景山区"},
            {"上海市", "黄浦区", "卢湾区", "徐汇区", "长宁区", "静安区", "普陀区", "闸北区", "虹口区", "杨浦区", "闵行区", "宝山区", "嘉定区", "浦东新区", "金山区", "松江区", "青浦区", "南汇区", "奉贤区"},
            {"天津市", "和平区", "河东区", "河西区", "南开区", "河北区", "红桥区"},
            {"重庆市", "渝中区", "九龙坡区", "江北区", "沙坪坝区", "南岸区", "大渡口区"},
            //华北地区 3
            {"河北省", "石家庄", "唐山", "秦皇岛", "邯郸", "邢台", "保定", "张家口", "承德", "沧州", "廊坊", "衡水"},
            {"山西省", "太原", "大同", "阳泉", "长治", "晋城", "朔州", "晋中", "运城", "忻州", "临汾", "吕梁"},
            {"内蒙古自治区", "呼和浩特", "包头", "乌海", "赤峰", "通辽", "鄂尔多斯", "呼伦贝尔", "巴彦淖尔", "乌兰察布", "兴安", "锡林郭勒", "阿拉善"},
            //东北地区 3
            {"辽宁省", "沈阳", "大连", "鞍山", "抚顺", "本溪", "丹东", "锦州", "营口", "阜新", "辽阳", "盘锦", "铁岭", "朝阳", "葫芦岛"},
            {"吉林省", "长春", "吉林", "四平", "辽源", "通化", "白山", "松原", "白城", "延边"},
            {"黑龙江", "哈尔滨", "齐齐哈尔", "鸡西", "鹤岗", "双鸭山", "大庆", "伊春", "佳木斯", "七台河", "牡丹江", "黑河", "绥化", "大兴安岭"},
            //华东地区 6
            {"江苏省", "南京", "无锡", "徐州", "常州", "苏州", "南通", "连云港", "淮安", "盐城", "扬州", "镇江", "泰州", "宿迁"},
            {"浙江省", "杭州", "宁波", "温州", "嘉兴", "湖州", "绍兴", "金华", "衢州", "舟山", "台州", "丽水"},
            {"安徽省", "合肥", "芜湖", "蚌埠", "淮南", "马鞍山", "淮北", "铜陵", "安庆", "黄山", "滁州", "阜阳", "宿州", "巢湖", "六安", "亳州", "池州", "宣城"},
            {"福建省", "福州", "厦门", "莆田", "三明", "泉州", "漳州", "南平", "龙岩", "宁德"},
            {"江西省", "南昌", "景德镇", "萍乡", "九江", "新余", "鹰潭", "赣州", "吉安", "宜春", "抚州", "上饶"},
            {"山东省", "济南", "青岛", "淄博", "枣庄", "东营", "烟台", "潍坊", "威海", "济宁", "泰安", "日照", "莱芜", "临沂", "德州", "聊城", "滨州", "菏泽"},
            //中南地区 6
            {"河南省", "郑州", "开封", "洛阳", "平顶山", "焦作", "鹤壁", "新乡", "安阳", "濮阳", "许昌", "漯河", "三门峡", "南阳", "商丘", "信阳", "周口", "驻马店"},
            {"湖北省", "武汉", "黄石", "襄樊", "十堰", "荆州", "宜昌", "荆门", "鄂州", "孝感", "咸宁", "随州", "恩施"},
            {"湖南省", "长沙", "株洲", "湘潭", "衡阳", "邵阳", "岳阳", "常德", "张家界", "益阳", "郴州", "永州", "怀化", "娄底", "湘西"},
            {"广东省", "广州", "深圳", "珠海", "汕头", "韶关", "佛山", "江门", "湛江", "茂名", "肇庆", "惠州", "梅州", "汕尾", "河源", "阳江", "清远", "东莞", "中山", "潮州", "揭阳", "云浮"},
            {"广西自治区", "南宁", "柳州", "桂林", "梧州", "北海", "防城港", "钦州", "贵港", "玉林", "百色", "贺州", "河池", "来宾", "崇左"},
            {"海南省", "海口", "三亚"},
            //西南地区 4
            {"四川省", "成都", "自贡", "攀枝花", "泸州", "德阳", "绵阳", "广元", "遂宁", "内江", "乐山", "南充", "宜宾", "广安", "达州", "眉山", "雅安", "巴中", "资阳", "阿坝", "甘孜", "凉山"},
            {"贵州省", "贵阳", "六盘水", "遵义", "安顺", "铜仁", "毕节", "黔西南", "黔东南", "黔南"},
            {"云南省", "昆明", "曲靖", "玉溪", "保山", "昭通", "丽江", "普洱", "临沧", "文山", "红河", "西双版纳", "楚雄", "大理", "德宏", "怒江", "迪庆"},
            {"西藏自治区", "拉萨", "昌都", "山南", "日喀则", "那曲", "阿里", "林芝"},
            //西北地区 5
            {"陕西省", "西安", "铜川", "宝鸡", "咸阳", "渭南", "延安", "汉中", "榆林", "安康", "商洛"},
            {"甘肃省", "兰州", "嘉峪关", "金昌", "白银", "天水", "武威", "张掖", "平凉", "酒泉", "庆阳", "定西", "陇南", "临夏", "甘南"},
            {"青海省", "西宁", "海东", "海北", "黄南", "海南", "果洛", "玉树", "海西"},
            {"宁夏自治区", "银川", "石嘴山", "吴忠", "固原", "中卫"},
            {"新疆自治区", "乌鲁木齐", "克拉玛依", "吐鲁番", "哈密", "和田", "阿克苏", "喀什", "克孜勒苏柯尔克孜", "巴音郭楞蒙古", "昌吉", "博尔塔拉蒙古", "伊犁哈萨克", "塔城", "阿勒泰"},
            //港澳台 3
            {"香港特别行政区", "北区", "大埔区", "东区", "观塘区", "黄大仙区", "九龙城区", "葵青区", "离岛区", "南区", "荃湾区", "沙田区", "深水埗区", "屯门区", "湾仔区", "西贡区", "油尖旺区", "元朗区", "中西区"},
            {"澳门特别行政区", "花地玛堂区", "圣安多尼堂区", "大堂区", "望德堂区", "风顺堂区", "嘉模堂区", "圣方济各堂区"},
            {"台湾省", "台北", "高雄", "基隆", "台中", "台南", "新竹", "嘉义"}
    };
}
