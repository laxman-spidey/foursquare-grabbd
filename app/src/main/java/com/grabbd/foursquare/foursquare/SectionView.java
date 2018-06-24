package com.grabbd.foursquare.foursquare;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SectionView extends LinearLayout {

    private boolean selected = false;
    public SectionView(Context context) {
        super(context);
    }

    public SectionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SectionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        addView(getSectionView(attrs));
    }


    public CardView layout;
    public CardView getSectionView(AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = (CardView) inflater.inflate(R.layout.section_view, this, false);
        ImageView icon = layout.findViewById(R.id.sectionIcon);
        TextView text = layout.findViewById(R.id.sectionText);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.sectionView, 0, 0);
        icon.setImageDrawable(a.getDrawable(R.styleable.sectionView_iconResource));
        text.setText(a.getText(R.styleable.sectionView_sectionLabel));
        layout.setOnClickListener(v -> {
            setSelected();
        });
        return layout;
    }

    public void setSelected() {
        ViewGroup viewGroup = (ViewGroup) this.getParent();
        int count = viewGroup.getChildCount();

        SectionView v = null;
        for(int i=0; i<count; i++) {
            v = (SectionView) viewGroup.getChildAt(i);
            v.layout.setCardBackgroundColor(getResources().getColor(R.color.white));
        }
        layout.setCardBackgroundColor(getResources().getColor(R.color.sectionSelected));
    }

    public interface onSelectedListener {
        void onSelected(String selectedText);
    }

}
