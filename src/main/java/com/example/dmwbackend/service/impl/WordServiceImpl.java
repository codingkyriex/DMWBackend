package com.example.dmwbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dmwbackend.config.AppHttpCodeEnum;
import com.example.dmwbackend.config.ResponseResult;
import com.example.dmwbackend.dto.WordDto;
import com.example.dmwbackend.mapper.FavoritesWordMapper;
import com.example.dmwbackend.mapper.UserMapper;
import com.example.dmwbackend.mapper.UserWordProgressMapper;
import com.example.dmwbackend.mapper.WordMapper;
import com.example.dmwbackend.pojo.*;
import com.example.dmwbackend.service.WordService;
import com.example.dmwbackend.util.LLMGenerator;
import com.example.dmwbackend.util.PromptGenerator;
import com.example.dmwbackend.util.SortUtil;
import com.example.dmwbackend.vo.UserWordVo;
import com.example.dmwbackend.vo.WordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: eric
 * @time: 2024/5/2 21:26
 */

@Service
public class WordServiceImpl extends ServiceImpl<WordMapper, Word> implements WordService {

    @Autowired
    private WordMapper wordMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserWordProgressMapper userWordProgressMapper;

    @Autowired
    private FavoritesWordMapper favoritesWordMapper;

    @Override
    public ResponseResult<Object> getAiSentence(Integer id) {
        Word word = wordMapper.selectById(id);
        String prompt = PromptGenerator.getAiSentencePrompt(word.getEnglish());
        Map<String, String> response = LLMGenerator.getResponse(prompt);
        String res = LLMGenerator.convertResponse(response);
        HashMap<String, String> nres = new HashMap<>();
        if (res.equals("error")) {
            return ResponseResult.errorResult(AppHttpCodeEnum.MISS_USER);
        }
        nres.put("sentence", res);
        return ResponseResult.okResult(nres);
    }

    @Override
    public ResponseResult<Object> getAiTest(Integer u) {
        User user = userMapper.selectById(u);
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.MISS_USER);
        }
        List<UserWordProgress> progress = userWordProgressMapper.getProgressById(u);
        if (progress.size() == 0) {
            List<Word> words = wordMapper.selectList(null);
            Collections.shuffle(words);
            Word word = wordMapper.selectById(words.get(0).getWordId());
            return ResponseResult.okResult(getSingleTest(word.getEnglish()));
        }
        // 使用Stream API进行排序和截取操作
        List<UserWordProgress> sortedList = progress.stream()
                .sorted((a, b) -> b.getDay().compareTo(a.getDay()))
                .limit(10)
                .collect(Collectors.toList());

        // 如果列表不足十个，则返回全部
        if (sortedList.size() < 10) {
            sortedList = progress.stream()
                    .sorted((a, b) -> b.getDay().compareTo(a.getDay()))
                    .collect(Collectors.toList());
        }
        UserWordProgress p = SortUtil.selectRandom(sortedList);
        Map<String, Object> res = null;
        Word word = wordMapper.selectById(p.getWordId());
        res = getSingleTest(word.getEnglish());
        return ResponseResult.okResult(res);
    }

    // 获取下一个要学习的单词（当前用户未学习过的单词）
    @Override
    public ResponseResult<WordVo> getNextWord(Integer userId) {
        // 获取该用户还未学过的一个单词
        Word word = wordMapper.getNextWord(userId);
        // 如果没有单词了，返回错误信息
        if (word == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NO_UNLEARNED_WORD);
        }
        // 更新用户的学习进度
        User user = userMapper.selectById(userId);
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.MISS_USER);
        }
        int progress = user.getProgress();
        progress++;
        user.setProgress(progress);
        userMapper.updateById(user);
        // 封装该单词的数据为WordVo对象
        WordVo wordVo = getWordVo(word.getWordId());
        wordVo.setProgress(progress);
        return ResponseResult.okResult(wordVo);
    }

    @Override
    public ResponseResult<Word> getWordDetail(Integer id) {
        Word word = wordMapper.selectById(id);
        if (word == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        return ResponseResult.okResult(word);
    }

    @Override
    public ResponseResult<Object> likeWord(Integer userId, Integer wordId) {
        // 查询用户是否已经收藏过这个单词
        Integer count = wordMapper.isLiked(userId, wordId);
        if (count > 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.IS_FAVORITED);
        }
        // 将单词添加到用户的收藏表favorites_word中
        // 获取当前的时间
        Date date = new Date();
        wordMapper.likeWord(userId, wordId, date);
        return ResponseResult.okResult("");

    }

    // 获取该用户要复习的单词列表
    @Override
    public ResponseResult<List<WordVo>> getReviewWords(Integer userId) {
        // 从user_word_progress表中获取2个学习状态为'forget'的单词
        List<Integer> reviewList = wordMapper.getReviewWords(userId);
        List<WordVo> reviewWords = new ArrayList<>();
        if (reviewList.isEmpty()) {
            // 从单词表中随机获取2个单词
            for (int i = 0; i < 2; i++) {
                Word word = wordMapper.getRandomWord();
                Integer wordId = word.getWordId();
                reviewList.add(wordId);
            }
        }

        for (Integer wordId : reviewList) {
            //将该单词的复习状态改为know(已复习)
            wordMapper.changeStatus(userId, wordId);
            WordVo wordVo = getWordVo(wordId);
            reviewWords.add(wordVo);
        }
        return ResponseResult.okResult(reviewWords);
    }

    @Override
    public ResponseResult<Object> getDailySentence() {
        Word word = wordMapper.getRandomWord();
        //调用AI生成句子
        ResponseResult<Object> sentence = getAiSentence(word.getWordId());
        return sentence;
    }

    @Override
    public ResponseResult<WordVo> getDailyWord() {
        //从数据库中随机获取一个单词
        Word word = wordMapper.getRandomWord();
        // 封装该单词的数据为WordVo对象
        WordVo wordVo = getWordVo(word.getWordId());
        return ResponseResult.okResult(wordVo);
    }

    @Override
    public ResponseResult<Object> getLikedWord(Integer userId) {
        List<FavoritesWord> likedWords = favoritesWordMapper.getLikedWords(userId);
        ArrayList<HashMap<String, Object>> res = new ArrayList<>();
        for (FavoritesWord word : likedWords) {
            HashMap<String, Object> map = new HashMap<>();
            Word word1 = wordMapper.selectById(word.getWordId());
            map.put("word_id", word1.getWordId());
            map.put("chinese", word1.getChinese());
            map.put("english", word1.getEnglish());
            res.add(map);
        }
        return ResponseResult.okResult(res);
    }

    @Override
    public ResponseResult<Vocabulary> getBooks() {
        List<Vocabulary> books = wordMapper.getBooks();
        return ResponseResult.okResult(books);
    }

    @Override
    public ResponseResult<Object> chooseBook(Integer userId, Integer bookId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.MISS_USER);
        }
        //判断单词书表中是否有该单词书
        Vocabulary book = wordMapper.selectBookById(bookId);
        if (book == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.MISS_BOOK);
        }
        user.setBook(bookId);
        userMapper.updateById(user);
        return ResponseResult.okResult("");
    }

    @Override
    public ResponseResult<Object> getUsersWords() {
        List<User> users = userMapper.selectList(null);
        ArrayList<UserWordVo> userWordVos = new ArrayList<>();
        for (User u : users) {
            ArrayList<Integer> forget = new ArrayList<>();
            ArrayList<Integer> know = new ArrayList<>();
            List<UserWordProgress> progress = userWordProgressMapper.getProgressById(u.getUserId());
            for (UserWordProgress p : progress) {
                long millisBetween = Math.abs(new Date().getTime() - p.getDay().getTime());

                // 将毫秒数转换为天数
                long daysBetween = millisBetween / (1000 * 60 * 60 * 24);
                if (Math.abs(daysBetween) >= 10) {
                    forget.add(p.getWordId());
                } else {
                    know.add(p.getWordId());
                }
            }
            userWordVos.add(UserWordVo.builder().userId(u.getUserId()).knowWord(know).forgetWord(forget).know(know.size()).total(know.size() + forget.size()).build());
        }
        return ResponseResult.okResult(userWordVos);
    }

    @Override
    public ResponseResult<Object> getAllWords(String word, Integer pageNum, Integer pageSize) {
        List<Word> words = wordMapper.selectList(null);
        Page<Word> page = new Page<>(pageNum, pageSize);
        if (Objects.equals(word, "")) {
            List<Word> records = wordMapper.selectPage(page, null).getRecords();
            return ResponseResult.okResult(records);
        }
        QueryWrapper<Word> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("english", word);
        List<Word> records = wordMapper.selectPage(page, queryWrapper).getRecords();
        HashMap<String, Object> res = new HashMap<>();
        res.put("record", records);
        res.put("num", words.size());
        return ResponseResult.okResult(res);
    }

    @Override
    public ResponseResult<Object> updateWord(Integer id, WordDto dto) {
        Word word = wordMapper.selectById(id);
        if (word == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.MISS_ITEM);
        }
        if (!Objects.equals(dto.getChinese(), "")) {
            word.setChinese(dto.getChinese());
        }
        if (!Objects.equals(dto.getEnglish(), "")) {
            word.setEnglish(dto.getEnglish());
        }
        if (!Objects.equals(dto.getProperty(), "")) {
            word.setProperty(dto.getProperty());
        }
        if (!Objects.equals(dto.getExample(), "")) {
            word.setExample(dto.getExample());
        }
        wordMapper.updateById(word);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    private Map<String, Object> getSingleTest(String word) {
        Random random = new Random();
        int randomInt = random.nextInt(101);
        String response = "";
        if (randomInt % 2 == 0) {
            response = LLMGenerator.convertResponse(LLMGenerator.getResponse(PromptGenerator.getSingleTestPrompt(word)));
        } else {
            response = LLMGenerator.convertResponse(LLMGenerator.getResponse(PromptGenerator.getCEGuessTest(word)));
        }
        Map<String, Object> dictionary = new HashMap<>();
        ArrayList<String> choices = new ArrayList<>();
        int answerIndex = -1; // 默认答案索引为-1，表示未找到答案

        // 使用正则表达式匹配问题
        String[] questionMatch = response.split("\\r?\\n")[0].split("Q: ");
        String question = questionMatch.length > 1 ? questionMatch[1].trim() : "";

        // 使用正则表达式匹配选项
        String[] options = response.split("\\r?\\n");
        for (int i = 1; i < options.length; i++) {
            String option = options[i].trim();
            String[] parts = option.split("\\. ");
            if (parts.length == 2) {
                choices.add(parts[1]); // 添加选项值到choices数组

                // 检查这个选项的值是否是要找的答案
                if (parts[1].equalsIgnoreCase(word)) {
                    answerIndex = choices.size() - 1; // 设置答案索引
                }
            }
        }

        // 将问题、选项和答案索引存储到字典中
        dictionary.put("question", question);
        dictionary.put("choices", choices);
        if (answerIndex != -1) {
            dictionary.put("answer", answerIndex); // 如果找到了答案，添加答案索引
        } else {
            dictionary.put("answer", null); // 如果没有找到答案，设置为null
        }
        return dictionary;
    }

    //封装WordVo对象
    private WordVo getWordVo(Integer wordId) {
        Word word = wordMapper.selectById(wordId);
        // 获取单词的发音
        String pronunciation = "https://fanyi.baidu.com/gettts?lan=en&text=" + word.getEnglish() + "&spd=3&source=web";
        // 获取单词的中文翻译
        String chinese = word.getChinese();
        String[] choices = new String[4];
        // 从数据库中随机获取三个错误的选项
        String[] wrongChoices = wordMapper.getWrongChoices(word.getWordId());
        // 将正确答案chinese插入到choice数组中随机位置（0或1或2或3）
        int answer = new Random().nextInt(4);
        choices[answer] = chinese;
        // 将错误答案插入到choice数组中
        for (int i = 0, j = 0; i < 4; i++) {
            if (choices[i] == null) {
                choices[i] = wrongChoices[j++];
            }
        }
        // 封装返回的数据为WordVo对象
        WordVo wordVo = WordVo.builder()
                .id(word.getWordId())
                .word(word.getEnglish())
                .progress(0)
                .pronunciation(pronunciation)
                .choice(choices)
                .answer(answer)
                .build();
        return wordVo;
    }


}
