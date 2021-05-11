import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Frontpageconfig e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.clearCookie('SESSION');
    cy.clearCookies();
    cy.intercept('GET', '/api/frontpageconfigs*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('frontpageconfig');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Frontpageconfigs', () => {
    cy.intercept('GET', '/api/frontpageconfigs*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('frontpageconfig');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Frontpageconfig').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Frontpageconfig page', () => {
    cy.intercept('GET', '/api/frontpageconfigs*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('frontpageconfig');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('frontpageconfig');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Frontpageconfig page', () => {
    cy.intercept('GET', '/api/frontpageconfigs*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('frontpageconfig');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Frontpageconfig');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Frontpageconfig page', () => {
    cy.intercept('GET', '/api/frontpageconfigs*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('frontpageconfig');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Frontpageconfig');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of Frontpageconfig', () => {
    cy.intercept('GET', '/api/frontpageconfigs*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('frontpageconfig');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Frontpageconfig');

    cy.get(`[data-cy="creationDate"]`).type('2021-05-10T10:11').invoke('val').should('equal', '2021-05-10T10:11');

    cy.get(`[data-cy="topNews1"]`).type('49367').should('have.value', '49367');

    cy.get(`[data-cy="topNews2"]`).type('10933').should('have.value', '10933');

    cy.get(`[data-cy="topNews3"]`).type('96262').should('have.value', '96262');

    cy.get(`[data-cy="topNews4"]`).type('50894').should('have.value', '50894');

    cy.get(`[data-cy="topNews5"]`).type('63949').should('have.value', '63949');

    cy.get(`[data-cy="latestNews1"]`).type('75441').should('have.value', '75441');

    cy.get(`[data-cy="latestNews2"]`).type('95739').should('have.value', '95739');

    cy.get(`[data-cy="latestNews3"]`).type('69490').should('have.value', '69490');

    cy.get(`[data-cy="latestNews4"]`).type('31818').should('have.value', '31818');

    cy.get(`[data-cy="latestNews5"]`).type('44496').should('have.value', '44496');

    cy.get(`[data-cy="breakingNews1"]`).type('81807').should('have.value', '81807');

    cy.get(`[data-cy="recentPosts1"]`).type('42146').should('have.value', '42146');

    cy.get(`[data-cy="recentPosts2"]`).type('18423').should('have.value', '18423');

    cy.get(`[data-cy="recentPosts3"]`).type('17520').should('have.value', '17520');

    cy.get(`[data-cy="recentPosts4"]`).type('2285').should('have.value', '2285');

    cy.get(`[data-cy="featuredArticles1"]`).type('5055').should('have.value', '5055');

    cy.get(`[data-cy="featuredArticles2"]`).type('3531').should('have.value', '3531');

    cy.get(`[data-cy="featuredArticles3"]`).type('45765').should('have.value', '45765');

    cy.get(`[data-cy="featuredArticles4"]`).type('295').should('have.value', '295');

    cy.get(`[data-cy="featuredArticles5"]`).type('74962').should('have.value', '74962');

    cy.get(`[data-cy="featuredArticles6"]`).type('14809').should('have.value', '14809');

    cy.get(`[data-cy="featuredArticles7"]`).type('30302').should('have.value', '30302');

    cy.get(`[data-cy="featuredArticles8"]`).type('67935').should('have.value', '67935');

    cy.get(`[data-cy="featuredArticles9"]`).type('46959').should('have.value', '46959');

    cy.get(`[data-cy="featuredArticles10"]`).type('33158').should('have.value', '33158');

    cy.get(`[data-cy="popularNews1"]`).type('18328').should('have.value', '18328');

    cy.get(`[data-cy="popularNews2"]`).type('76299').should('have.value', '76299');

    cy.get(`[data-cy="popularNews3"]`).type('43455').should('have.value', '43455');

    cy.get(`[data-cy="popularNews4"]`).type('57076').should('have.value', '57076');

    cy.get(`[data-cy="popularNews5"]`).type('87118').should('have.value', '87118');

    cy.get(`[data-cy="popularNews6"]`).type('48678').should('have.value', '48678');

    cy.get(`[data-cy="popularNews7"]`).type('6469').should('have.value', '6469');

    cy.get(`[data-cy="popularNews8"]`).type('6850').should('have.value', '6850');

    cy.get(`[data-cy="weeklyNews1"]`).type('63484').should('have.value', '63484');

    cy.get(`[data-cy="weeklyNews2"]`).type('37160').should('have.value', '37160');

    cy.get(`[data-cy="weeklyNews3"]`).type('7850').should('have.value', '7850');

    cy.get(`[data-cy="weeklyNews4"]`).type('11110').should('have.value', '11110');

    cy.get(`[data-cy="newsFeeds1"]`).type('96361').should('have.value', '96361');

    cy.get(`[data-cy="newsFeeds2"]`).type('86878').should('have.value', '86878');

    cy.get(`[data-cy="newsFeeds3"]`).type('54823').should('have.value', '54823');

    cy.get(`[data-cy="newsFeeds4"]`).type('69338').should('have.value', '69338');

    cy.get(`[data-cy="newsFeeds5"]`).type('27541').should('have.value', '27541');

    cy.get(`[data-cy="newsFeeds6"]`).type('29851').should('have.value', '29851');

    cy.get(`[data-cy="usefulLinks1"]`).type('33775').should('have.value', '33775');

    cy.get(`[data-cy="usefulLinks2"]`).type('50956').should('have.value', '50956');

    cy.get(`[data-cy="usefulLinks3"]`).type('47913').should('have.value', '47913');

    cy.get(`[data-cy="usefulLinks4"]`).type('70258').should('have.value', '70258');

    cy.get(`[data-cy="usefulLinks5"]`).type('12521').should('have.value', '12521');

    cy.get(`[data-cy="usefulLinks6"]`).type('93856').should('have.value', '93856');

    cy.get(`[data-cy="recentVideos1"]`).type('51377').should('have.value', '51377');

    cy.get(`[data-cy="recentVideos2"]`).type('77343').should('have.value', '77343');

    cy.get(`[data-cy="recentVideos3"]`).type('9597').should('have.value', '9597');

    cy.get(`[data-cy="recentVideos4"]`).type('14301').should('have.value', '14301');

    cy.get(`[data-cy="recentVideos5"]`).type('30539').should('have.value', '30539');

    cy.get(`[data-cy="recentVideos6"]`).type('42842').should('have.value', '42842');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/frontpageconfigs*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('frontpageconfig');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of Frontpageconfig', () => {
    cy.intercept('GET', '/api/frontpageconfigs*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/frontpageconfigs/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('frontpageconfig');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('frontpageconfig').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/frontpageconfigs*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('frontpageconfig');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
