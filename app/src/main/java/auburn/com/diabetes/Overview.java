package auburn.com.diabetes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

/**
 * Created by victorm on 4/12/16.
 */
public class Overview extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    private SliderLayout slider;
    private ImageView pieImage;
    private ImageView lineImage;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nav_overview,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initializeComponent();
    }

    private class PageofSlider {
        String name;
        String image;
        String url;
        public PageofSlider(String name,String image,String url) {
            this.name = name;
            this.image = image;
            this.url = url;
        }
    }

    private void initializeComponent() {
        pieImage = (ImageView)getView().findViewById(R.id.piechartImage) ;
        lineImage = (ImageView)getView().findViewById(R.id.linechartImage) ;
        pieImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),Piechart.class));
            }
        });
        lineImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),Linechart.class));
            }
        });
        slider = (SliderLayout)getView().findViewById(R.id.slider);
        PageofSlider[] pages = new PageofSlider[4];
        pages[0] = new PageofSlider("About Diabetes","http://www.chunzhangmo.com/dbt/diabetes1.jpg","http://www.chunzhangmo.com/diabetes");
        pages[1] = new PageofSlider("Diabetes Education","http://www.chunzhangmo.com/dbt/diabetes2.jpg","http://www.diabetes.org");
        pages[2] = new PageofSlider("Diabetes Resource","http://www.chunzhangmo.com/dbt/diabetes3.jpg","http://www.diabetes.org");
        pages[3] = new PageofSlider("Ask for Help","http://www.chunzhangmo.com/dbt/diabetes4.jpg","http://www.diabetes.org");

        for(PageofSlider ps : pages) {
            TextSliderView textSliderView = new TextSliderView(getContext());
            // initialize a SliderLayout
            textSliderView
                    .description(ps.name)
                    .image(ps.image)
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("url",ps.url);
            slider.addSlider(textSliderView);
        }


        slider.setPresetTransformer(SliderLayout.Transformer.CubeIn);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(3000);
        slider.addOnPageChangeListener(this);

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        String url =(String)slider.getBundle().get("url");
        Intent intent = new Intent(getActivity(),Advertisements.class);
        intent.putExtra("url",url);
        startActivity(intent);
        Toast.makeText(getContext(),slider.getBundle().get("url") + "", Toast.LENGTH_SHORT).show();
    }
}
