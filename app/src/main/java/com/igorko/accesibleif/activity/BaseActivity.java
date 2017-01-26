package com.igorko.accesibleif.activity;

import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.igorko.accesibleif.R;
import com.igorko.accesibleif.utils.Const;
import com.igorko.accesibleif.utils.ScreenUtils;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * Created by Igorko on 11.10.2016.
 */

public abstract class BaseActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, Const {

    private Dialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initProgress();
    }

    protected Toolbar initToolbar(String title){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        return toolbar;
    }

    protected Drawer.Result initLeftDrawer(String appTitle, int selectedMenuPosition) {
        Drawer drawer = new Drawer()
                .withActivity(this)
                .withToolbar(initToolbar(appTitle))
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .withSelectedItem(selectedMenuPosition)
                .withOnDrawerItemClickListener(mOnClickDrawerListener)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_all).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_hospital).withIcon(getMyDrawable(R.drawable.hospital_ic)).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_pharmacy).withIcon(getMyDrawable(R.drawable.pharmacy_ic)).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_shop).withIcon(getMyDrawable(R.drawable.shop_ic)).withIdentifier(1),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(getMyDrawable(R.drawable.settings_ic)).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_about).withIcon(getMyDrawable(R.drawable.about_icon)).withIdentifier(1)
                );

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            int drawerWith = ScreenUtils.getScreenWidth(BaseActivity.this) / 3;
            drawer.withDrawerWidthPx(drawerWith);
        }else if(ScreenUtils.isTablet() && getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            int drawerWith = ScreenUtils.getScreenWidth(BaseActivity.this) / 2;
            drawer.withDrawerWidthPx(drawerWith);
        }

        return drawer.build();
    }

    Drawer.OnDrawerItemClickListener mOnClickDrawerListener = new Drawer.OnDrawerItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
            onDrawerMenuItemSelected(position);
        }
    };

    protected abstract void onDrawerMenuItemSelected(int position);

    public Drawable getMyDrawable(int id) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(id, getTheme());
            return drawable;
        } else {
            drawable = getResources().getDrawable(id);
            return drawable;
        }
    }

    private void initProgress() {
        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.progress_dialog);
    }

    protected void showProgress(){
        if(mDialog != null) {
            mDialog.show();
        }
    }

    protected void hideProgress(){
        if(mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
