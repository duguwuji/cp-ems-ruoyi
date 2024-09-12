package com.cpems.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cpems.common.utils.StringUtils;
import com.cpems.system.domain.vo.ItemTopologyVo;
import com.cpems.system.mapper.ItemTopologyMapper;
import lombok.RequiredArgsConstructor;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import com.cpems.system.domain.bo.CameraConfigBo;
import com.cpems.system.domain.vo.CameraConfigVo;
import com.cpems.system.domain.CameraConfig;
import com.cpems.system.mapper.CameraConfigMapper;
import com.cpems.system.service.ICameraConfigService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 视频配置Service业务层处理
 *
 * @author ruoyi
 * @date 2023-04-04
 */
@RequiredArgsConstructor
@Service
public class CameraConfigServiceImpl implements ICameraConfigService {

    private final CameraConfigMapper baseMapper;
    private final ItemTopologyMapper itemTopologyMapper;

    /**
     * 查询视频配置
     */
    @Override
    public CameraConfigVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询视频配置列表
     */
    @Override
    public TableDataInfo<CameraConfigVo> queryPageList(CameraConfigBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<CameraConfig> lqw = buildQueryWrapper(bo);
        Page<CameraConfigVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        List<CameraConfigVo> cameraConfigVos = result.getRecords();
        for (CameraConfigVo cameraConfigVo : cameraConfigVos) {
            ItemTopologyVo itemTopologyVo = itemTopologyMapper.selectVoById(cameraConfigVo.getAreaId());
            if(ObjectUtil.isNotNull(itemTopologyVo)) {
                cameraConfigVo.setArea(itemTopologyVo);
            }
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询视频配置列表
     */
    @Override
    public List<CameraConfigVo> queryList(CameraConfigBo bo) {
        LambdaQueryWrapper<CameraConfig> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<CameraConfig> buildQueryWrapper(CameraConfigBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<CameraConfig> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getCameraName()), CameraConfig::getCameraName, bo.getCameraName());
        lqw.eq(StringUtils.isNotBlank(bo.getCameraBrand()), CameraConfig::getCameraBrand, bo.getCameraBrand());
        lqw.eq(StringUtils.isNotBlank(bo.getCameraSn()), CameraConfig::getCameraSn, bo.getCameraSn());
        lqw.eq(StringUtils.isNotBlank(bo.getCameraToken()), CameraConfig::getCameraToken, bo.getCameraToken());
        lqw.eq(StringUtils.isNotBlank(bo.getCameraKey()), CameraConfig::getCameraKey, bo.getCameraKey());
        lqw.eq(StringUtils.isNotBlank(bo.getCameraSecret()), CameraConfig::getCameraSecret, bo.getCameraSecret());
        lqw.eq(bo.getAreaId()!=null, CameraConfig::getAreaId, bo.getAreaId());
        return lqw;
    }

    /**
     * 新增视频配置
     */
    @Override
    public Boolean insertByBo(CameraConfigBo bo) {
        CameraConfig add = BeanUtil.toBean(bo, CameraConfig.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改视频配置
     */
    @Override
    public Boolean updateByBo(CameraConfigBo bo) {
        CameraConfig update = BeanUtil.toBean(bo, CameraConfig.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(CameraConfig entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除视频配置
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 通过序列号返回摄像头播放地址
     */
    @Override
    public CameraConfigVo getUrlBySerialNumber(String serialNumber){
        CameraConfigVo result = baseMapper.selectVoOne(new LambdaQueryWrapper<CameraConfig>().eq(CameraConfig::getCameraSn,serialNumber));
        String token = getAccessToken(result.getCameraKey(), result.getCameraSecret());
        result.setCameraToken(token);
        String url = getUrl(token, result.getCameraSn());
        result.setUrl(url);
        result.setCameraKey("");
        result.setCameraSecret("");
        return result;
    }

    public static String getAccessToken(String AppKey, String Secret) {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 封装参数
        StringBuffer params = new StringBuffer();
        params.append("appKey=" + AppKey);
        params.append("&");
        params.append("appSecret=" + Secret);

        // 创建Post请求
        HttpPost httpPost = new HttpPost("https://open.ys7.com/api/lapp/token/get" + "?" + params);
        // 设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");

        // 响应模型
        CloseableHttpResponse response = null;

        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null) {
                //返回数据
                String responseString = EntityUtils.toString(responseEntity);
                //生成实体类（responseString就可以看到数据的）
                JSONObject jsonObject = JSONObject.fromObject(responseString);
                //Token t = (Token) JSONObject.toBean(jsonObject, Token.class);
                JSONObject data = jsonObject.getJSONObject("data");
                return data.getString("accessToken");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static String getUrl(String accessToken,String deviceSerial) {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 封装参数
        StringBuffer params = new StringBuffer();
        params.append("accessToken=" + accessToken);
        params.append("&");
        params.append("deviceSerial=" + deviceSerial);


        // 创建Post请求
        HttpPost httpPost = new HttpPost("https://open.ys7.com/api/lapp/v2/live/address/get" + "?" + params);
        // 设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");

        // 响应模型
        CloseableHttpResponse response = null;

        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null) {
                //返回数据
                String responseString = EntityUtils.toString(responseEntity);
                //生成实体类（responseString就可以看到数据的）
                JSONObject jsonObject = JSONObject.fromObject(responseString);
                //Token t = (Token) JSONObject.toBean(jsonObject, Token.class);
                JSONObject data = jsonObject.getJSONObject("data");
                return data.getString("url");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
