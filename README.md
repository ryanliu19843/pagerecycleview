"# pagerecycleview" 
pagerecycleview 添加下拉刷新，上拉加载，stickyheaders效果。

加载动画，重写了adapter 使 recycleview支持 headview和footview。

修改了adapter绑定view的过程，使viewhold使用更简洁。

统一了数据源，所有数据源实现都变成card，card类型对应一个viewhold，使用更简洁

配合我的android studio插件，可以根据layout自动生成 viewhold 和card 以及card的数字id。使用更简单

使用介绍

如果使用我封装的数据交互框架，可以使用工具生成的请求类和映射类。


     mMFRecyclerView.setOnSwipLoadListener(new OnPageSwipListener(getContext(), new  ApiUpdateApi().set("com.udows.xdt",1,"ios","sss","0",""), new DfText()));
 
 其中apiupdateapi为数据请求类，set()设置交互参数
 dftext()为数据整理类。
 

如果使用自己的交互协议代码如下，方法为异步处理。返回cardadapter

     new SwipSyncTask() {
            @Override
            protected CardAdapter doInBackground(Object... params) {   
              /  try {
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
                if (iserror) {
                    error = "加载错误";
                    return null;
                }
                this.haspage=ispage;
                return new CardAdapter(MainActivity.this, list);
            }

            @Override
            public void intermit() {

            }
        };
        onPageSwipListener = new OnPageSwipListener(swipSyncTask);
        
        
  设置异步处理类
  
         recycleview.setOnSwipLoadListener(onPageSwipListener);
         
  实现下拉加载
  
        recycleview.pullLoad();
        
  
        
