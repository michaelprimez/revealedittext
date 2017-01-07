package gr.escsoft.michaelprimez.revealedittext;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.joanzapata.iconify.widget.IconTextView;

import gr.escsoft.michaelprimez.revealedittext.interfaces.RevealEditTextListener;
import gr.escsoft.michaelprimez.revealedittext.tools.EditCursorColor;

/**
 * Created by michael on 12/31/16.
 */

public class RevealEditText extends FrameLayout implements View.OnClickListener {

    private static final int DefaultAnimationDuration = 400;
    private ViewState mViewState = ViewState.ShowingRelealedLayout;

    private final CardView mRevealContainerCardView;
    private final TextView mRevealValueTextView;
    private final IconTextView mStartEditImageView;

    private final CardView mContainerCardView;
    private final AppCompatEditText mActualEditText;
    private final IconTextView mDoneEditImageView;

    private Context mContext;

    private RevealEditTextListener mRevealEditTextListener;

    /* Attributes */
    private @ColorInt int mRevealViewBackgroundColor;
    private @ColorInt int mRevealViewTextColor;
    private @ColorInt int mStartEditTintColor;
    private @ColorInt int mEditViewBackgroundColor;
    private @ColorInt int mEditViewTextColor;
    private @ColorInt int mDoneEditTintColor;
    private int mAnimDuration;
    private boolean mShowIcons;
    private String mTextIfEmpty;
    private String mText;

    public enum ViewState {
        ShowingRelealedLayout,
        ShowingEditLayout,
        ShowingAnimation
    }

    static {
        Iconify.with(new MaterialModule());
    }

    public RevealEditText(@NonNull Context context) {
        this(context, null);
    }

    public RevealEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RevealEditText(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RevealEditText(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;

        getAttributeSet(attrs, defStyleAttr, defStyleRes);

        final LayoutInflater factory = LayoutInflater.from(context);
        factory.inflate(R.layout.view_reveal_edit_text, this);

        mRevealContainerCardView = (CardView) findViewById(R.id.CrdVw_RevealContainer);
        mRevealValueTextView = (TextView) findViewById(R.id.TxtVw_Value);
        mStartEditImageView = (IconTextView) findViewById(R.id.ImgVw_StartEdit);

        mContainerCardView = (CardView) findViewById(R.id.CrdVw_Container);
        mActualEditText = (AppCompatEditText)findViewById(R.id.EdtTxt_ActualEditText);
        mDoneEditImageView = (IconTextView) findViewById(R.id.ImgVw_DoneEdit);

        init();
    }

    public RevealEditText(Builder builder) {
        super(builder.mContext);
        this.mContext = builder.mContext;

        final LayoutInflater factory = LayoutInflater.from(mContext);
        factory.inflate(R.layout.view_reveal_edit_text, this);

        mRevealContainerCardView = (CardView) findViewById(R.id.CrdVw_RevealContainer);
        mRevealValueTextView = (TextView) findViewById(R.id.TxtVw_Value);
        mStartEditImageView = (IconTextView) findViewById(R.id.ImgVw_StartEdit);

        mContainerCardView = (CardView) findViewById(R.id.CrdVw_Container);
        mActualEditText = (AppCompatEditText)findViewById(R.id.EdtTxt_ActualEditText);
        mDoneEditImageView = (IconTextView) findViewById(R.id.ImgVw_DoneEdit);

        this.mRevealEditTextListener = builder.mRevealEditTextListener;

        this.mRevealViewBackgroundColor = builder.mRevealViewBackgroundColor;
        this.mRevealViewTextColor = builder.mRevealViewTextColor;
        this.mStartEditTintColor = builder.mStartEditTintColor;
        this.mEditViewBackgroundColor = builder.mEditViewBackgroundColor;
        this.mEditViewTextColor = builder.mEditViewTextColor;
        this.mDoneEditTintColor = builder.mDoneEditTintColor;
        this.mAnimDuration = builder.mAnimDuration;
        this.mShowIcons = builder.mShowIcons;
        this.mTextIfEmpty = builder.mTextIfEmpty;
        this.mText = builder.mText;
        init();
    }

    private void init() {
        setupColors();

        if (!mShowIcons) {
            mStartEditImageView.setVisibility(View.GONE);
            mDoneEditImageView.setVisibility(View.GONE);
        }
        mRevealValueTextView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                if (mViewState == ViewState.ShowingRelealedLayout) {
                    revealEditView();
                    return true;
                }
                return false;
            }
        });
        setText(mText);
        if (TextUtils.isEmpty(mText) && !TextUtils.isEmpty(mTextIfEmpty)) {
            mRevealValueTextView.setText(mTextIfEmpty);
        }
        mStartEditImageView.setOnClickListener(this);
        mDoneEditImageView.setOnClickListener(this);

        clearAnimation();
        clearFocus();
    }

    private void getAttributeSet(@Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        if (attrs != null) {
            TypedArray attributes = mContext.getTheme().obtainStyledAttributes(attrs, R.styleable.RevealEditText, defStyleAttr, defStyleRes);
            mRevealViewBackgroundColor = attributes.getColor(R.styleable.RevealEditText_RevealViewBackgroundColor, Color.WHITE);
            mRevealViewTextColor = attributes.getColor(R.styleable.RevealEditText_RevealViewTextColor, Color.BLACK);
            mStartEditTintColor = attributes.getColor(R.styleable.RevealEditText_StartEditTintColor, Color.GRAY);
            mEditViewBackgroundColor = attributes.getColor(R.styleable.RevealEditText_EditViewBackgroundColor, Color.WHITE);
            mEditViewTextColor = attributes.getColor(R.styleable.RevealEditText_EditViewTextColor, Color.BLACK);
            mDoneEditTintColor = attributes.getColor(R.styleable.RevealEditText_DoneEditTintColor, Color.GRAY);
            mAnimDuration = attributes.getColor(R.styleable.RevealEditText_AnimDuration, DefaultAnimationDuration);
            mShowIcons = attributes.getBoolean(R.styleable.RevealEditText_ShowIcons, true);
            mTextIfEmpty = attributes.getString(R.styleable.RevealEditText_TextIfEmpty);
            mText = attributes.getString(R.styleable.RevealEditText_Text);
        }
    }

    private void setupColors() {
        mRevealContainerCardView.setBackgroundColor(mRevealViewBackgroundColor);
        mRevealValueTextView.setBackgroundColor(mRevealViewBackgroundColor);
        mRevealValueTextView.setTextColor(mRevealViewTextColor);
        mStartEditImageView.setBackgroundColor(mRevealViewBackgroundColor);
        mStartEditImageView.setTextColor(mStartEditTintColor);

        mContainerCardView.setBackgroundColor(mEditViewBackgroundColor);
        mActualEditText.setBackgroundColor(mEditViewBackgroundColor);
        mActualEditText.setTextColor(mEditViewTextColor);
        mActualEditText.setHintTextColor(mStartEditTintColor);
        EditCursorColor.setCursorColor(mActualEditText, mEditViewTextColor);
        mDoneEditImageView.setBackgroundColor(mEditViewBackgroundColor);
        mDoneEditImageView.setTextColor(mDoneEditTintColor);
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        if (TextUtils.isEmpty(mText) && !TextUtils.isEmpty(mTextIfEmpty)) {
            mRevealValueTextView.setText(mTextIfEmpty);
        } else {
            mText = text;
            mRevealValueTextView.setText(text);
            mActualEditText.setText(text);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ImgVw_StartEdit) {
            revealEdit();
        } else if (id == R.id.ImgVw_DoneEdit) {
            hideEdit();
        }
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        if (!gainFocus && mViewState == ViewState.ShowingEditLayout) {
            hideEditView();
        }
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    public void revealEdit() {
        if (mViewState == ViewState.ShowingRelealedLayout) {
            revealEditView();
        }
    }

    public void hideEdit() {
        if (mViewState == ViewState.ShowingEditLayout) {
            hideEditView();
        }
    }

    private void revealEditView() {
        mViewState = ViewState.ShowingAnimation;
        int cx = mRevealContainerCardView.getLeft();
        int cxr = mRevealContainerCardView.getRight();
        int cy = (mRevealContainerCardView.getTop() + mRevealContainerCardView.getHeight())/2;
        int reverse_startradius = Math.max(mRevealContainerCardView.getWidth(), mRevealContainerCardView.getHeight());
        int reverse_endradius = 0;

        final Animator revealAnimator = ViewAnimationUtils.createCircularReveal(mRevealContainerCardView, cx, cy, reverse_startradius, reverse_endradius);
        revealAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mViewState = ViewState.ShowingEditLayout;
                mRevealContainerCardView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        final Animator animator = ViewAnimationUtils.createCircularReveal(mContainerCardView, cxr, cy, reverse_endradius, reverse_startradius);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mContainerCardView.setVisibility(View.VISIBLE);
                mViewState = ViewState.ShowingEditLayout;
                ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mContainerCardView.setVisibility(View.VISIBLE);
        animator.setDuration(mAnimDuration);
        revealAnimator.setDuration(mAnimDuration);

        animator.start();
        revealAnimator.start();

    }

    private void hideEditView() {
        mViewState = ViewState.ShowingAnimation;
        int cx = mContainerCardView.getLeft();
        int cxr = mContainerCardView.getRight();
        int cy = (mContainerCardView.getTop() + mContainerCardView.getHeight())/2;
        int reverse_startradius = Math.max(mContainerCardView.getWidth(), mContainerCardView.getHeight());
        int reverse_endradius = 0;

        final Animator revealAnimator = ViewAnimationUtils.createCircularReveal(mRevealContainerCardView, cx, cy, reverse_endradius, reverse_startradius);
        revealAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mViewState = ViewState.ShowingRelealedLayout;
                mRevealContainerCardView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        final Animator animator = ViewAnimationUtils.createCircularReveal(mContainerCardView, cxr, cy, reverse_startradius, reverse_endradius);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mContainerCardView.setVisibility(View.INVISIBLE);
                mViewState = ViewState.ShowingRelealedLayout;
                ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mActualEditText.getWindowToken(), 0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mRevealContainerCardView.setVisibility(View.VISIBLE);
        animator.setDuration(mAnimDuration);
        animator.start();

        revealAnimator.setDuration(mAnimDuration);
        revealAnimator.start();
    }

    public void setRevealEditTextListener(RevealEditTextListener revealEditTextListener) {
        mRevealEditTextListener = revealEditTextListener;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.mViewState = mViewState;
        ss.mRevealViewBackgroundColor = mRevealViewBackgroundColor;
        ss.mRevealViewTextColor = mRevealViewTextColor;
        ss.mStartEditTintColor = mStartEditTintColor;
        ss.mEditViewBackgroundColor = mEditViewBackgroundColor;
        ss.mEditViewTextColor = mEditViewTextColor;
        ss.mDoneEditTintColor = mDoneEditTintColor;
        ss.mAnimDuration = mAnimDuration;
        ss.mShowIcons = mShowIcons;
        ss.mTextIfEmpty = mTextIfEmpty;
        ss.mText = mText;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mViewState = ss.mViewState;
        mRevealViewBackgroundColor = ss.mRevealViewBackgroundColor;
        mRevealViewTextColor = ss.mRevealViewTextColor;
        mStartEditTintColor = ss.mStartEditTintColor;
        mEditViewBackgroundColor = ss.mEditViewBackgroundColor;
        mEditViewTextColor = ss.mEditViewTextColor;
        mDoneEditTintColor = ss.mDoneEditTintColor;
        mAnimDuration = ss.mAnimDuration;
        mShowIcons = ss.mShowIcons;
        mTextIfEmpty = ss.mTextIfEmpty;
        mText = ss.mText;
        if (mViewState == ViewState.ShowingEditLayout) {
            mContainerCardView.setVisibility(View.VISIBLE);
            mRevealContainerCardView.setVisibility(View.INVISIBLE);
        } else {
            mContainerCardView.setVisibility(View.INVISIBLE);
            mRevealContainerCardView.setVisibility(View.VISIBLE);
        }
        if (!mShowIcons) {
            mStartEditImageView.setVisibility(View.GONE);
            mDoneEditImageView.setVisibility(View.GONE);
        }
        setText(mText);
    }

    static class SavedState extends BaseSavedState {
        ViewState mViewState;
        int mRevealViewBackgroundColor;
        int mRevealViewTextColor;
        int mStartEditTintColor;
        int mEditViewBackgroundColor;
        int mEditViewTextColor;
        int mDoneEditTintColor;
        int mAnimDuration;
        boolean mShowIcons;
        String mTextIfEmpty;
        String mText;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            mViewState = ViewState.values()[in.readInt()];
            mRevealViewBackgroundColor = in.readInt();
            mRevealViewTextColor = in.readInt();
            mStartEditTintColor = in.readInt();
            mEditViewBackgroundColor = in.readInt();
            mEditViewTextColor = in.readInt();
            mDoneEditTintColor = in.readInt();
            mAnimDuration = in.readInt();
            mShowIcons = in.readInt() > 0 ? true : false;
            mTextIfEmpty = in.readString();
            mText = in.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(mViewState.ordinal());
            out.writeInt(mRevealViewBackgroundColor);
            out.writeInt(mRevealViewTextColor);
            out.writeInt(mStartEditTintColor);
            out.writeInt(mEditViewBackgroundColor);
            out.writeInt(mEditViewTextColor);
            out.writeInt(mDoneEditTintColor);
            out.writeInt(mAnimDuration);
            out.writeInt(mShowIcons ? 1 : 0);
            out.writeString(mTextIfEmpty);
            out.writeString(mText);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public static class Builder {
        private Context mContext;
        private ViewState mViewState = ViewState.ShowingRelealedLayout;
        private @ColorInt int mRevealViewBackgroundColor = Color.WHITE;
        private @ColorInt int mRevealViewTextColor = Color.BLACK;
        private @ColorInt int mStartEditTintColor = Color.GRAY;
        private @ColorInt int mEditViewBackgroundColor = Color.GRAY;
        private @ColorInt int mEditViewTextColor = Color.WHITE;
        private @ColorInt int mDoneEditTintColor = Color.WHITE;
        private int mAnimDuration = DefaultAnimationDuration;
        private boolean mShowIcons = true;
        private String mTextIfEmpty;
        private String mText;

        private RevealEditTextListener mRevealEditTextListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setViewState(ViewState viewState) {
            this.mViewState = viewState;
            return this;
        }

        public Builder setRevealViewBackgroundColor(int revealViewBackgroundColor) {
            this.mRevealViewBackgroundColor = revealViewBackgroundColor;
            return this;
        }

        public Builder setRevealViewTextColor(int revealViewTextColor) {
            this.mRevealViewTextColor = revealViewTextColor;
            return this;
        }

        public Builder setStartEditTintColor(int startEditTintColor) {
            this.mStartEditTintColor = startEditTintColor;
            return this;
        }

        public Builder setEditViewBackgroundColor(int editViewBackgroundColor) {
            this.mEditViewBackgroundColor = editViewBackgroundColor;
            return this;
        }

        public Builder setEditViewTextColor(int editViewTextColor) {
            this.mEditViewTextColor = editViewTextColor;
            return this;
        }

        public Builder setDoneEditTintColor(int doneEditTintColor) {
            this.mDoneEditTintColor = doneEditTintColor;
            return this;
        }

        public Builder setAnimDuration(int animDuration) {
            this.mAnimDuration = animDuration;
            return this;
        }

        public Builder setShowIcons(boolean showIcons) {
            this.mShowIcons = showIcons;
            return this;
        }

        public Builder setTextIfEmpty(String textIfEmpty) {
            this.mTextIfEmpty = textIfEmpty;
            return this;
        }

        public Builder setText(String text) {
            this.mText = text;
            return this;
        }

        public Builder setRevealEditTextListener(RevealEditTextListener revealEditTextListener) {
            mRevealEditTextListener = revealEditTextListener;
            return this;
        }

        public RevealEditText build() {
            return new RevealEditText(this);
        }
    }
}
