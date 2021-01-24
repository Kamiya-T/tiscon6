package com.tiscon.controller;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.tiscon.dao.EstimateDao;
import com.tiscon.dto.UserOrderDto;
import com.tiscon.form.UserOrderForm;
import com.tiscon.form.SimpleOrderForm;
import com.tiscon.service.Distance;
import com.tiscon.service.EstimateService;
import com.tiscon.service.PostalCodeService;
import com.tiscon.service.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

/**
 * 引越し見積もりのコントローラークラス。
 *
 * @author Oikawa Yumi
 */
@Controller
public class EstimateController {

    private final EstimateDao estimateDAO;

    private final EstimateService estimateService;

    /**
     * コンストラクタ
     *
     * @param estimateDAO EstimateDaoクラス
     * @param estimateService EstimateServiceクラス
     */
    public EstimateController(EstimateDao estimateDAO, EstimateService estimateService) {
        this.estimateDAO = estimateDAO;
        this.estimateService = estimateService;
    }

    @GetMapping("")
    String index(Model model) {
        return "top";
    }

    /**
     * 入力画面に遷移する。
     *
     * @param model 遷移先に連携するデータ
     * @return 遷移先
     */
    // @GetMapping("input")
    /*
    String inputaa(Model model) {
        if (!model.containsAttribute("userOrderForm")) {
            model.addAttribute("userOrderForm", new UserOrderForm());
        }

        model.addAttribute("prefectures", estimateDAO.getAllPrefectures());
        return "input";
    }
    */

    /**
     * 入力画面に遷移する。Simple Version
     *
     * @param model 遷移先に連携するデータ
     * @return 遷移先
     */
    @GetMapping("simple_input")
    String input(Model model) {
        if (!model.containsAttribute("SimpleOrderForm")) {
            model.addAttribute("SimpleOrderForm", new SimpleOrderForm());
        }
        model.addAttribute("prefectures", estimateDAO.getAllPrefectures());
        return "simple_input";
    }

    /**
     * 簡易的なお見積り画面に遷移する。
     *
     * @param model 遷移先に連携するデータ
     * @return 遷移先
     */
    // param修正！！
    // html value
    @PostMapping(value = "simple_result", params = "calculation")
    String simple_calculation(@Validated SimpleOrderForm simpleOrderForm, BindingResult result, Model model) {
        PostalCodeService pcs = new PostalCodeService();
        Response response1;
        Response response2;
        int dis;

        if (result.hasErrors()) {
            model.addAttribute("prefectures", estimateDAO.getAllPrefectures());
            model.addAttribute("simpleOrderForm", simpleOrderForm);
            return "simple_input";
        }

        try {
            // 埼玉県さいたま市の住所を取得
            response1 = pcs.getResponse("3300854");
            // 埼玉県所沢市の住所を取得
            response2 = pcs.getResponse("3590000");
            dis = Distance.getDistance(response1.getLatitude(), response1.getLongitude(), response2.getLatitude(), response2.getLongitude());
        } catch (IOException e) {
            // networkによるエラー発生時
            // simple_inputに返してもエラーが表示されないので、処理を追加する必要あり
            return "simple_input";
        } catch (Exception e) {
            // 存在しない郵便番号が入力された時
            // simple_inputに返してもエラーが表示されないので、処理を追加する必要あり
            return "simple_input";
        }
        /*
        // 料金の計算を行う。
        UserOrderForm userOrderForm = new UserOrderForm();
        userOrderForm = estimateService.simpleToUser(simpleOrderForm);
        UserOrderDto dto = new UserOrderDto();
        */
        //この関数動作する？
        //BeanUtils.copyProperties(userOrderForm, dto);
        Integer price[] = estimateService.getPriceFromDistance(simpleOrderForm, dis);

        model.addAttribute("prefectures", estimateDAO.getAllPrefectures());
        model.addAttribute("SimpleOrderForm", simpleOrderForm);
        model.addAttribute("price", price[0]);
        model.addAttribute("distancePrice", price[1]);
        model.addAttribute("cargoPrice", price[2]);
        model.addAttribute("optionPrice", price[3]);
        return "simple_result";
    }

    /**
     * simple_resultから詳細入力画面に遷移。
     *
     * @param sof 顧客が入力した見積もり依頼情報
     * @param model         遷移先に連携するデータ
     * @return 遷移先
     */
    // 以下、入力！！
    @PostMapping(value = "input", params = "input")
    String sendInput(SimpleOrderForm sof, Model model) {
        UserOrderForm uof = new UserOrderForm();
        uof.setBicycle(sof.getBicycle());

        uof.setOldPostalCode(sof.getOldPostalCode());
        uof.setNewPostalCode(sof.getNewPostalCode());
        uof.setMovingDate(sof.getMovingDate());

        uof.setBox(sof.getBox());
        uof.setBed(sof.getBed());
        uof.setBicycle(sof.getBicycle());
        uof.setWashingMachine(sof.getWashingMachine());
        uof.setWashingMachineInstallation(sof.getWashingMachineInstallation());

        model.addAttribute("userOrderForm", uof);
        model.addAttribute("prefectures", estimateDAO.getAllPrefectures());
        return "input";
    }
    /*
     public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOldPrefectureId() {
        return oldPrefectureId;
    }

    public void setOldPrefectureId(String oldPrefectureId) {
        this.oldPrefectureId = oldPrefectureId;
    }

    public String getOldAddress() {
        return oldAddress;
    }

    public void setOldAddress(String oldAddress) {
        this.oldAddress = oldAddress;
    }

    public String getNewPrefectureId() {
        return newPrefectureId;
    }

    public void setNewPrefectureId(String newPrefectureId) {
        this.newPrefectureId = newPrefectureId;
    }

    public String getNewAddress() {
        return newAddress;
    }

    public void setNewAddress(String newAddress) {
        this.newAddress = newAddress;
    }

    public String getBox() {
        return box;
    }

    public void setBox(String box) {
        this.box = box;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public String getBicycle() {
        return bicycle;
    }

    public void setBicycle(String bicycle) {
        this.bicycle = bicycle;
    }

    public String getWashingMachine() {
        return washingMachine;
    }

    public void setWashingMachine(String washingMachine) {
        this.washingMachine = washingMachine;
    }

    public boolean getWashingMachineInstallation() {
        return washingMachineInstallation;
    }

    public void setWashingMachineInstallation(boolean washingMachineInstallation) {
        this.washingMachineInstallation = washingMachineInstallation;
    }
    public String getMovingDate() {
        return movingDate;
    }

    public void setMovingDate(String movingDate) { this.movingDate = movingDate; }
}


     */
    /**
     * TOP画面に戻る。
     *
     * @param model 遷移先に連携するデータ
     * @return 遷移先
     */
    @PostMapping(value = "submit", params = "backToTop")
    String backToTop(Model model) {
        return "top";
    }

    /**
     * 確認画面に遷移する。
     *
     * @param userOrderForm 顧客が入力した見積もり依頼情報
     * @param model         遷移先に連携するデータ
     * @return 遷移先
     */
    @PostMapping(value = "submit", params = "confirm")
    String confirm(UserOrderForm userOrderForm, Model model) {
        model.addAttribute("prefectures", estimateDAO.getAllPrefectures());
        model.addAttribute("userOrderForm", userOrderForm);
        return "confirm";
    }

    /**
     * 入力画面に戻る。
     *
     * @param userOrderForm 顧客が入力した見積もり依頼情報
     * @param model         遷移先に連携するデータ
     * @return 遷移先

    @PostMapping(value = "result", params = "backToInput")
    String backToInput(UserOrderForm userOrderForm, Model model) {
        model.addAttribute("prefectures", estimateDAO.getAllPrefectures());
        model.addAttribute("userOrderForm", userOrderForm);
        return "input";
    }
     */
    /**
     * 確認画面に戻る。
     *
     * @param userOrderForm 顧客が入力した見積もり依頼情報
     * @param model         遷移先に連携するデータ
     * @return 遷移先
     */
    @PostMapping(value = "order", params = "backToConfirm")
    String backToConfirm(UserOrderForm userOrderForm, Model model) {
        model.addAttribute("prefectures", estimateDAO.getAllPrefectures());
        model.addAttribute("userOrderForm", userOrderForm);
        return "confirm";
    }

    /**
     * 概算見積もり画面に遷移する。
     *
     * @param userOrderForm 顧客が入力した見積もり依頼情報
     * @param result        精査結果
     * @param model         遷移先に連携するデータ
     * @return 遷移先
     */
    @PostMapping(value = "result", params = "calculation")
    String calculation(@Validated UserOrderForm userOrderForm, BindingResult result, Model model) {
        if (result.hasErrors()) {

            model.addAttribute("prefectures", estimateDAO.getAllPrefectures());
            model.addAttribute("userOrderForm", userOrderForm);
            return "simple_result";
        }
        // 料金の計算を行う。
        UserOrderDto dto = new UserOrderDto();
        BeanUtils.copyProperties(userOrderForm, dto);
        Integer price[] = estimateService.getPrice(dto);

        model.addAttribute("prefectures", estimateDAO.getAllPrefectures());
        model.addAttribute("userOrderForm", userOrderForm);
        model.addAttribute("price", price[0]);
        model.addAttribute("distancePrice",price[1]);
        model.addAttribute("cargoPrice",price[2]);
        model.addAttribute("optionPrice",price[3]);
        return "result";
    }

    /**
     * 申し込み完了画面に遷移する。
     *
     * @param userOrderForm 顧客が入力した見積もり依頼情報
     * @param result        精査結果
     * @param model         遷移先に連携するデータ
     * @return 遷移先
     */
    @PostMapping(value = "order", params = "complete")
    String complete(@Validated UserOrderForm userOrderForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("prefectures", estimateDAO.getAllPrefectures());
            model.addAttribute("userOrderForm", userOrderForm);
            return "confirm";
        }

        UserOrderDto dto = new UserOrderDto();
        BeanUtils.copyProperties(userOrderForm, dto);
        estimateService.registerOrder(dto);

        return "complete";
    }

}
