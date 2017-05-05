## 功能介绍
- 为recycleview实现下拉刷新，分页加载，stickyhead效果，headview和footview功能，错误提示等功能
- 重新adapter统一为cardadapter，使用更简单
- 统一数据源，每一项为一个card，card类型与viewhold绑定编写更方便
- 修改viewhold的加载方式，使其与card的类型对应，用户无需处理bind和create的关系。


## 实现效果
 
![图片](https://github.com/ryanliu19843/pagerecycleview/blob/master/ezgif-2-052447.gif)

## 实现代码
### 如果使用我实现的服务器数据交互框架实现方式如下

```Java
ApiUpdateApi api=new ApiUpdateApi().set("sss",1,"ios","sss","0","");
mMFRecyclerView.setOnSwipLoadListener(new OnPageSwipListener(getContext(), api, new DfText()));
mMFRecyclerView.reload();
```
- -- ApiUpdateApi 为根据接口定义自动生成的接口请求类，set方法为接口设置参数
- -- mMFRecyclerView 为本项目中的MFRecyclerview

### 如果需要自己实现数据交互实现方法如下

```Java
swipSyncTask = new SwipSyncTask() {
	@Override
  protected CardAdapter doInBackground(Object... params) {
       try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
             e.printStackTrace();
        }
         List<Card> list = new ArrayList<>();
         for (int i = 0; i < 25; i++) {
             if (i % 10 == 0 && isgroup) {
                   CardGroup card = new CardGroup("group" + i);
                   card.setShowType(1);
                   list.add(card);
             } else {
                   CardText card = new CardText("1111" + i);
                   list.add(card);
             }
          }
          if (iserror) {  //设置错误信息
              error = "加载错误";
              return null;
          }
          this.haspage=ispage;   //设置是否有下一页
          return new CardAdapter(MainActivity.this, list);  //返回adapter
       }

       @Override
       public void intermit() {

       }
 };
onPageSwipListener = new OnPageSwipListener(swipSyncTask);
recycleview.setOnSwipLoadListener(onPageSwipListener);
recycleview.pullLoad();  //下拉加载数据
				
```
- --setshowtype（1）为设置该项显示为stickyhead效果
- --doInBackground 为异步加载

### 项目的card和viewhold可以使用我写android studio插件根据layout自动生产，只需要修改标注括号部分部分
### card部分
```Java
public class CardText extends Card<【【String】】> {
	
	public CardText(String item) {
	    this.setItem(item);
    	this.type = com.mdx.ryan.pagetest.R.string.id_text;
    }

     @Override
     public void bind(RecyclerView.ViewHolder viewHolder, int posion) {
        Text item = (Text) viewHolder;
        item.set(posion, this);
        this.lastitem = null;
     }
}

```
### viewhold部分
```Java
public class Text extends BaseItem{
    public LinearLayout contentView;
    public TextView text;



    public Text(View itemView, Context context) {
        super(itemView);
        this.context=context;
        initView();
    }

    @SuppressLint("InflateParams")
    public static Text getView(Context context, ViewGroup parent) {
        LayoutInflater flater = LayoutInflater.from(context);
        View convertView;
        if (parent == null) {
            convertView = flater.inflate(R.layout.item_text, null);
        } else {
            convertView = flater.inflate(R.layout.item_text, parent, false);
        }
        return new Text(convertView, context);
    }

    private void initView() {
    	findVMethod();
    }

    private void findVMethod(){
        contentView=(LinearLayout)findViewById(R.id.contentView);
        text=(TextView)findViewById(R.id.text);


    }

    public void set(int posion,CardText card){
        this.card=card;
        【【this.text.setText(card.item);】】
    }
}
```

- 如果项目中有任何问题请邮箱联系我 youyexianhe@126.com
